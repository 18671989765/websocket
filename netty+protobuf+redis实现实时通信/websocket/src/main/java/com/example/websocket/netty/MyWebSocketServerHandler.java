package com.example.websocket.netty;

import com.example.websocket.redis.RedisUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    private RedisTemplate redisTemplate = RedisUtils.redisTemplate();

    private static String keyCount = "count";


    private WebSocketServerHandshaker handshaker;

    /**
     *  客户端与服务端建立了socket 链接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("客户端与服务端连接开启：" + ctx.channel().remoteAddress().toString());
        // 建立链接之后维护channel
        ChannelGroup group = Global.group;
        group.add(ctx.channel());
        redisTemplate.opsForValue().increment(keyCount, 1);
        logger.info("当前在线人数：" + redisTemplate.opsForValue().get(keyCount));

        //上下线向所有在线用户推送在线人数
    }



    /**
     * 客户端与服务端链接断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 断开链接后移除，这里要测试如果网络自动断开是否要删除 ctx
        Global.group.remove(ctx.channel());
        logger.info("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
        redisTemplate.opsForValue().decrement(keyCount, 1);
        logger.info("当前在线人数：" + redisTemplate.opsForValue().get(keyCount));

        //上下线向所有在线用户推送在线人数
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "："  + "当前在线人数："+redisTemplate.opsForValue().get(keyCount));
        // 群发
        Global.group.writeAndFlush(tws);



    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //是否只接受socket 请求
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, ((FullHttpRequest) msg));
            // WebSocket接入
        } else if (msg instanceof WebSocketFrame) {
            if ("anzhuo".equals(ctx.attr(AttributeKey.valueOf("type")).get())) {
                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
            } else {
                handlerWebSocketFrame2(ctx, (WebSocketFrame) msg);
            }
        }
    }

    /**
     * channel 通道 Read 读取 Complete 完成 在通道读取完成后会在这个方法里通知，对应可以做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {

            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.info("仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        logger.info("服务端收到客户端数据：" + request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", ctx.channel(), request));
        }
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
        // 群发
        Global.group.writeAndFlush(tws);
//        返回【谁发的发给谁】
//        ctx.channel().writeAndFlush(tws);
    }

    private void handlerWebSocketFrame2(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            logger.info("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", frame.getClass().getName()));
        }
        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        logger.info("服务端2收到客户端数据：" + request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(String.format("%s received %s", ctx.channel(), request));
        }
        TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request + "当前在线人数："+redisTemplate.opsForValue().get(keyCount));
        // 群发

        Global.group.writeAndFlush(tws);
//        返回【谁发的发给谁】
//      ctx.channel().writeAndFlush(tws);
        return;
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 如果HTTP解码失败，返回HHTP异常
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //获取url后置参数
        HttpMethod method = req.getMethod();
        String uri = req.getUri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        System.out.println(parameters.get("request").get(0));
        if (method == HttpMethod.GET && "/websocket".equals(uri)) {
            //....处理
            ctx.attr(AttributeKey.valueOf("type")).set("anzhuo");
        } else if (method == HttpMethod.GET && "/websocket".equals(uri)) {
            //...处理
            ctx.attr(AttributeKey.valueOf("type")).set("live");
        }
        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://" + req.headers().get(HttpHeaders.Names.HOST) + uri, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //TODO
        super.channelUnregistered(ctx);

    }

    /**
     * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常处理
        cause.printStackTrace();
        ctx.close();
    }

}

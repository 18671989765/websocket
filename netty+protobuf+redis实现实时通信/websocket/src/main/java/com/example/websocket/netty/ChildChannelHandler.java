package com.example.websocket.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel e) throws Exception {
        ChannelPipeline pipeline = e.pipeline();

      // HttpServerCodec：将请求和应答消息解码为HTTP消息
        pipeline.addLast("http-codec", new HttpServerCodec());

      // HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));


      // ChunkedWriteHandler：向客户端发送HTML5文件
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());

        //添加websocket路由,加入配置文件中
        pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));

        // 在管道中添加我们自己的接收数据实现方法
        pipeline.addLast("handler", new MyWebSocketServerHandler());

        /**
         IdleStateHandler 是netty处理空闲状态的处理器
         1.readerIdleTime : 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
         2.writerIdleTime : 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
         3.allIdleTime : 表示多长时间没有读和写，就会发送一个心跳检测包检测是否连接
         4.当有空闲的时候，就会触发 IdleStateEvent 事件
         5.当IdleStateEvent触发后，就会传递给管道的下一个handler去处理，
         通过回调触发，下一个handler的userEventTiggered,在该方法中去处理当IdleStateEvent（读空闲，写空闲，读写空闲）
         */
        pipeline.addLast(new IdleStateHandler(60,40,100, TimeUnit.SECONDS));
        // 加入一个对空闲检测进一步处理的自定义handler
        pipeline.addLast(new HeartServerHandler());

    }
}

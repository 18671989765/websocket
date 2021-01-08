package com.example.websocket.entity;

import io.protostuff.Tag;


/**
 * 使用protoBuf的序列化方式
 */
public class ProtobufEntity {

    @Tag(1)
    private String userId;
    @Tag(2)
    private String phone;
    @Tag(3)
    private String projectId;
    @Tag(4)
    private String roomId;
    @Tag(5)
    private String roomNum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public ProtobufEntity(String userId, String phone, String projectId, String roomId, String roomNum) {
        this.userId = userId;
        this.phone = phone;
        this.projectId = projectId;
        this.roomId = roomId;
        this.roomNum = roomNum;
    }

    @Override
    public String toString() {
        return "ProtobufEntity{" +
                "userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                ", projectId='" + projectId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomNum='" + roomNum + '\'' +
                '}';
    }
}

package com.example.websocket.entity;

import io.protostuff.Tag;

import java.io.Serializable;

public class Send_Message implements Serializable {

    //定义字段顺序
    @Tag(1)
    private String userId;

    @Tag(2)
    private String phone;

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

    public Send_Message(String userId, String phone) {
        this.userId = userId;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "send_message{" +
                "userId='" + userId + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

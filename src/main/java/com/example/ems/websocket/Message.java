package com.example.ems.websocket;

import com.alibaba.fastjson.JSON;
import com.example.ems.pojo.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


//{time:'xxx',code:1,fromName:'xxx',toName:'xxx',type:'text',text:'2'}
@Data
public class Message {
    String time;
    int code;
    User user;//发消息的人的信息，因为有群聊，所以这里是一个user类而不是fromName，可以琢磨一下
    String toName;//发送给谁
    String type;//消息类型
    Object text;//消息内容

    public String turnToString() {
        return JSON.toJSONString(this);
    }
}

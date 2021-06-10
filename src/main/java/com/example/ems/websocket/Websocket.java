package com.example.ems.websocket;

import com.alibaba.fastjson.JSON;
import com.example.ems.pojo.User;
import com.example.ems.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * @author zrl
 * @date 2020-12-23 15:23
 */
@Component
@ServerEndpoint(value = "/chat/{userName}/{headImage}")
@Slf4j
public class Websocket {
    private Session session;
    //存储websocket
    public static Map<String,Websocket> webSocketMap = new ConcurrentHashMap<>();
    //存储users
    public static List<User> userList = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(@PathParam("userName") String userName,
                       @PathParam("headImage") String headImage,
                       Session session) {
        this.session = session;
        webSocketMap.put(userName,this);//this就是当前的Websocket
        log.info("有新的客户端连接，用户名为{}，头像为{}，当前总数为：{}",userName,headImage,webSocketMap.size());
        //准备发送消息
        Message message = new Message();
        //时间
        message.setTime(TimeUtils.getTime());
        //有用户上线
        message.setCode(1);
        //用户
        User user = new User();
        user.setUserName(userName);
        user.setHeadImage("common/face/"+headImage);
        userList.add(user);
        message.setText(userList);
        //群发
        broadcastMessage(message);
    }

    @OnClose
    public void onClose(@PathParam("userName") String userName,Session session) {
        //准备发送消息
        Message message = new Message();
        //时间
        message.setTime(TimeUtils.getTime());
        //有用户下线
        message.setCode(0);
        //删除下线的用户

        for(User user:userList) {
            if (user.getUserName().equals(userName)) {
                userList.remove(user);
            }
        }

        message.setText(userList);
        webSocketMap.remove(userName);
        log.info("{}下线，当前总数为：{}",userName,webSocketMap.size());
        //群发
        broadcastMessage(message);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【收到消息：{}】",message);
        //反序列化
        Message message1 = JSON.parseObject(message, Message.class);
        //填上code
        message1.setCode(11);
        //发送消息
        sendMessage(message1);
    }

    //发送单个消息
    public void sendMessage(Message message) {
        Websocket ChatRoom = webSocketMap.get(message.getToName());
        if (ChatRoom != null) {
            try {
                ChatRoom.session.getBasicRemote().sendText(message.turnToString());
                log.info("【websocket发送消息成功，{}--->{}，内容是: {}】",
                        message.getUser().getUserName(),message.getToName(),message.getText());
            } catch (IOException e) {
                e.printStackTrace();
                log.error("【websocket消息发送失败，{}--->{}，内容是: {}】",
                        message.getUser().getUserName(),message.getToName(),message.getText());
            }
        } else {
            log.error("【聊天室不存在】");
        }
    }

    //群发消息
    public void broadcastMessage(Message message) {
        for (String name: webSocketMap.keySet() ) {
            try {
                webSocketMap.get(name).session.getBasicRemote().sendText(message.turnToString());
                log.info("【websocket群发消息成功，内容是: {}】",CodeStore.codeMap.get(message.getCode()));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("【websocket群发消息失败】");
            }
        }
    }
}


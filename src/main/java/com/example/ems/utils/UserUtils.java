package com.example.ems.utils;

import com.example.ems.pojo.User;
import com.example.ems.websocket.Websocket;

import java.util.ArrayList;

public class UserUtils {
    public static ArrayList<String> getAllUser(){
        ArrayList<String> userNameList = new ArrayList<>();
        for (User user : Websocket.userList) {
            userNameList.add(user.getUserName());
        }
        return userNameList;
    }
}

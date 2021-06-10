package com.example.ems.controller;

import com.example.ems.pojo.Result;
import com.example.ems.pojo.User;
import com.example.ems.websocket.Websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
public class UserController {
    @RequestMapping("/login")
    public Object longin(@RequestBody User user, HttpSession httpSession) {
        System.out.println("UserController进行登陆验证！");
        Result result =  new Result();

        if(!Websocket.webSocketMap.keySet().contains(user.getUserName())) {
            //设置flag表示登陆成功
            result.setFlag(true);
            //做记录
            httpSession.setAttribute("userName",user.getUserName());
        } else {
            result.setFlag(false);
            result.setMessage("登陆失败，用户名已存在!");
        }
        return result;
    }

    //提供接口获取所有用户
    @GetMapping("/user/list")
    public Object list() {
        return Websocket.userList;
    }
}

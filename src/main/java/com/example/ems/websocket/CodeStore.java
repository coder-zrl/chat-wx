package com.example.ems.websocket;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;


public class CodeStore {
    public static ConcurrentHashMap<Integer,String> codeMap = new ConcurrentHashMap<>();
    static {
        codeMap.put(0,"用户下线");
        codeMap.put(1,"用户上线");
    }
}

package com.example.ems.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
}

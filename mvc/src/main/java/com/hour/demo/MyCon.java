package com.hour.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hour.annotation.HourAutowired;
import com.hour.annotation.HourController;
import com.hour.annotation.HourRequestMapping;
import com.hour.annotation.HourRequestParam;

@HourController
@HourRequestMapping("/my")
public class MyCon {

    @HourAutowired
    public MyService myService;

    @HourRequestMapping("/show")
    public void show(HttpServletRequest req, HttpServletResponse resp, @HourRequestParam("name") String name){
        if(name == null){
            name = "hour";
        }
        try {
            resp.getWriter().write(myService.get(name));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

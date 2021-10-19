package com.hour.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hour.annotation.HourAutowired;
import com.hour.annotation.HourController;
import com.hour.annotation.HourRequestMapping;
import com.hour.annotation.HourRequestParam;

@HourController
@HourRequestMapping("my")
public class MyCon {

    @HourAutowired
    private MyService myService;

    @HourRequestMapping("show")
    public String show(HttpServletRequest req, HttpServletResponse resp, @HourRequestParam("name") String name){
        return myService.get(name);
    }
}

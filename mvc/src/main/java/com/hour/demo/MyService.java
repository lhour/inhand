package com.hour.demo;

import com.hour.mvc.annotation.HourService;

@HourService
public class MyService implements IMyService{
    public String get(String name) {
        return "my name is " + name;
    }
}

package com.hour.demo.bean;

import java.util.ArrayList;

import com.hour.ioc.annotation.Component;
import com.hour.ioc.annotation.Configuration;
import com.hour.ioc.annotation.Value;

@Component
public class StudentList {
    
    @Value("128")
    private int capcity;


    private ArrayList<Student> list = new ArrayList<>();

    /**
     * @return the capcity
     */
    public int getCapcity() {
        return capcity;
    }

    /**
     * @param capcity the capcity to set
     */
    public void setCapcity(int capcity) {
        this.capcity = capcity;
    }

}

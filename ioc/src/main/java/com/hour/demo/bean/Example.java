package com.hour.demo.bean;

import com.hour.demo.config.MySqlConfig;
import com.hour.ioc.annotation.Autowired;
import com.hour.ioc.annotation.Bean;
import com.hour.ioc.annotation.Configuration;

@Configuration
public class Example {
    
    @Autowired
    MySqlConfig mySqlConfig;

    @Autowired
    StudentList studentList;

    @Autowired("lisi")
    Student lisi;

    @Bean("lisi")
    public Student getLisi(){
        Student lisi = new Student();
        lisi.setAge(18);
        lisi.setId("1011");
        lisi.setName("lisi");
        return lisi;
    }

    @Bean("zhangsan")
    public Student getZhangsan(){
        return new Student();
    }
    
}

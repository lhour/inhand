package com.hour.demo;

import com.hour.demo.config.MySqlConfig;
import com.hour.ioc.annotation.Autowired;
import com.hour.ioc.core.Factory;
import com.hour.ioc.core.FactoryBean;

public class App {
    
    //默认factory
    @Autowired
    static Factory factory;

    //自定义bean
    @Autowired("mySqlConfig")
    static MySqlConfig mysql; //这个地方不规定

    public static void main(String[] args) {
        FactoryBean.load();
        System.out.print(mysql.getPort());
    }

}

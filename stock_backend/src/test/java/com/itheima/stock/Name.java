package com.itheima.stock;

import org.springframework.beans.factory.annotation.Value;

public class Name {
    @Value("${appName}")
    private String appName;
    public Name(){
        System.out.println(appName);
    }
}

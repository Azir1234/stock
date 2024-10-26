package com.itheima.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class TestPasswordEncoder {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void Test(){
        String pwd="1234";
        String token=passwordEncoder.encode(pwd);
        System.out.println(passwordEncoder.encode(pwd));
        System.out.println(passwordEncoder.encode(pwd));
        System.out.println(passwordEncoder.encode(pwd));
        System.out.println(token);
        Boolean b=passwordEncoder.matches(pwd,token);
        System.out.println(b);
    }
}

package com.example.child1.impl;

import com.example.child1.spi.HelloService;

/**
 * User: JY
 * Date: 2020/5/6 0006
 * Description:
 */
public class HelloImpl1 implements HelloService {
    @Override
    public String hello(String name) {
        return "HelloImpl1" + name;
    }
}

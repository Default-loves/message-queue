package com.junyi;

import java.util.ServiceLoader;

/**
 * User: JY
 * Date: 2020/5/6 0006
 * Description:
 */

public class Main {
    public static void main(String[] args) {
        ServiceLoader<DemoService> shouts = ServiceLoader.load(DemoService.class);
        for (DemoService s : shouts) {
            s.sayHi("main");
        }
    }
}
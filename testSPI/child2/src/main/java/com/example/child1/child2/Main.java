package com.example.child1.child2;

import com.example.child1.spi.DemoService;
import com.example.child1.spi.HelloService;

import java.util.ServiceLoader;

/**
 * User: JY
 * Date: 2020/5/6 0006
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<DemoService> load = ServiceLoader.load(DemoService.class);
        for (DemoService s : load) {
            System.out.println("for:");
            System.out.println(s.sayHi("main"));
        }
        System.out.println("Bye");

//        ServiceLoader<HelloService> load = ServiceLoader.load(HelloService.class);
//        for (HelloService s : load) {
//            System.out.println("for:");
//            System.out.println(s.hello("main"));;
//        }
//        System.out.println("Bye");
    }
}

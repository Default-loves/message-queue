package com.junyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者
 * @time: 2021/3/11 16:02
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@SpringBootApplication
public class KafkaProducer{
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducer.class, args);
    }
}

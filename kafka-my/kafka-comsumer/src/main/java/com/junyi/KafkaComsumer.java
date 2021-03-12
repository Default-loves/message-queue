package com.junyi;

import com.junyi.entity.UserChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * 消费者
 * @time: 2021/3/11 16:14
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@SpringBootApplication
@Slf4j
public class KafkaComsumer{

    public static final String topic = "jy.user";



    public static void main(String[] args) {
        SpringApplication.run(KafkaComsumer.class, args);
    }

    @KafkaListener(topics = topic)
    public void handlerEvent(UserChangeEvent event) {
        log.info("receive message: {}", event.toString());
    }

}

package com.junyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 死信处理
 * @time: 2021/3/11 17:54
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@SpringBootApplication
@Slf4j
public class DeadLetterApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeadLetterApplication.class, args);
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMQMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}

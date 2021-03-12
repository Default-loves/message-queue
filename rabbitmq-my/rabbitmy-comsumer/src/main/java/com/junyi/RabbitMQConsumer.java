package com.junyi;

import com.junyi.entity.UserChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @time: 2021/3/11 17:54
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@SpringBootApplication
@Slf4j
public class RabbitMQConsumer {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQConsumer.class, args);
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMQMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @RabbitListener(queues = MyParameter.QUEUE_USER)
    public void handlerEvent(UserChangeEvent event) {
        log.info("Receive message from listener: {}", event.toString());
    }

}

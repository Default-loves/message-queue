package com.junyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @time: 2021/3/12 10:34
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@RestController
@Slf4j
public class MyController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("receive")
    public void receive() {
        Article event = (Article) rabbitTemplate.receiveAndConvert(RabbitMQConsumer.QUEUE);
        if (event != null) {
            log.info("Receive message by hand:{}", event.toString());
        } else {
            log.info("No more message~");
        }
    }
}

package com.junyi;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



/**
 * @time: 2021/3/12 10:27
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@RestController
public class MyController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public void send(@RequestBody Article data) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE, data);
    }
}

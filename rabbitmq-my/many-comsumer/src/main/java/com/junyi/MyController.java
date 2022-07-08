package com.junyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @time: 2022/6/21 14:51
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("msg")
public class MyController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("/")
    public void sendMessage() {
        String data = UUID.randomUUID().toString();
        log.info("send data: {}", data);
        rabbitTemplate.convertAndSend(ConfigurationMy.EXCHANGE, ConfigurationMy.ROUTE_KEY, data);
    }
}

package com.junyi;

import com.junyi.entity.UserChangeEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.junyi.MyParameter.QUEUE_USER;

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
    public void send(@RequestBody UserChangeEvent event) {
        rabbitTemplate.convertAndSend(QUEUE_USER, event);
    }
}

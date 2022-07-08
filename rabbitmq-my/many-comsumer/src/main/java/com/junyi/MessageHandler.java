package com.junyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @time: 2022/6/21 14:55
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Component
@Slf4j
public class MessageHandler {

    @RabbitListener(queues = ConfigurationMy.QUEUE)
    public void memberService(String data) {
        log.info("receive message: {}", data);

    }
}

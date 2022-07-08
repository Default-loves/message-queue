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

    // 正常消息处理
    @RabbitListener(queues = ConfigurationMy.QUEUE)
    public void memberService(String data) {
        log.info("receive message: {}", data);
        // 由于抛出异常，导致消息成为死信
        throw new NullPointerException("error");
    }

    // 死信处理方法
    @RabbitListener(queues = ConfigurationMy.DEAD_QUEUE)
    public void memberServiceDead(String data) {
        log.info("dead message: {}", data);

    }
}

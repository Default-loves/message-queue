package com.junyi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息接收，这儿是为了简单，直接将两个服务的实例，总共4个实例的接收消息放在一起了，
 * @time: 2022/6/21 14:55
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Component
@Slf4j
public class MessageHandler {

    // 服务A实例1
    @RabbitListener(queues = ConfigurationMy.QUEUE_SERVICE1)
    public void memberServiceA1(String data) {
        log.info("service A instance 1 receive message: {}", data);
    }

    // 服务A实例2
    @RabbitListener(queues = ConfigurationMy.QUEUE_SERVICE1)
    public void memberServiceA2(String data) {
        log.info("service A instance 2 receive message: {}", data);
    }

    // 服务B实例1
    @RabbitListener(queues = ConfigurationMy.QUEUE_SERVICE2)
    public void memberServiceB1(String data) {
        log.info("service B instance 1 receive message: {}", data);
    }

    // 服务B实例2
    @RabbitListener(queues = ConfigurationMy.QUEUE_SERVICE2)
    public void memberServiceB2(String data) {
        log.info("service B instance 2 receive message: {}", data);
    }

}

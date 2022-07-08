package com.junyi;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @time: 2022/6/21 14:49
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Configuration
public class ConfigurationMy {

    public static final String QUEUE_SERVICE1 = "queue-001";        // 服务A绑定的队列
    public static final String QUEUE_SERVICE2 = "queue-002";        // 服务B绑定的队列
    public static final String EXCHANGE = "fanout-exchange-001";
    public static final String ROUTE_KEY = "key-001";

    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_SERVICE1);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_SERVICE2);
    }

    @Bean
    public Declarables declarables() {
        FanoutExchange exchange = new FanoutExchange(EXCHANGE);     // 广播型 Exchange
        return new Declarables(queue1(), queue2(), exchange,
                BindingBuilder.bind(queue1()).to(exchange),
                BindingBuilder.bind(queue2()).to(exchange));
    }
}

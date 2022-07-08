package com.junyi;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

    public static final String QUEUE = "queue-001";
    public static final String EXCHANGE = "exchange-001";
    public static final String ROUTE_KEY = "key-001";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Declarables declarables() {
        DirectExchange exchange = new DirectExchange(EXCHANGE);
        return new Declarables(queue(), exchange,
                BindingBuilder.bind(queue()).to(exchange).with(ROUTE_KEY));
    }
}

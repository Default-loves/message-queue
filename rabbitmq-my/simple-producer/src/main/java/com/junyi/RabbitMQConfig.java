package com.junyi;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @time: 2021/3/12 10:36
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "queue-001";
    public static final String EXCHANGE = "exchange-001";
    public static final String ROUTE_KEY = "key-001";

    @Bean
    public Queue SpringCssDirectQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange SpringCssDirectExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(SpringCssDirectQueue()).to(SpringCssDirectExchange())
                .with(ROUTE_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMQMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

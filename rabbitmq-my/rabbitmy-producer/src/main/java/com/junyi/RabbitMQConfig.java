package com.junyi;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.junyi.MyParameter.*;

/**
 * @time: 2021/3/12 10:36
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue SpringCssDirectQueue() {
        return new Queue(QUEUE_USER, true);
    }

    @Bean
    public DirectExchange SpringCssDirectExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(SpringCssDirectQueue()).to(SpringCssDirectExchange())
                .with(ROUTE_USER);
    }

    @Bean
    public Jackson2JsonMessageConverter rabbitMQMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

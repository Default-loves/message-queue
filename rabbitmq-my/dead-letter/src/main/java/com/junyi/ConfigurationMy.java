package com.junyi;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * @time: 2022/6/21 14:49
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Configuration
public class ConfigurationMy {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static final String QUEUE = "queue-001";
    public static final String EXCHANGE = "exchange-001";
    public static final String ROUTE_KEY = "key-001";

    // 死信交换器、队列和路由Key
    public static final String DEAD_EXCHANGE = "dead";
    public static final String DEAD_QUEUE = "dead";
    public static final String DEAD_ROUTING_KEY = "dead";

    // 正常队列
    @Bean
    public Declarables declarables() {
        Queue queue = new Queue(QUEUE);
        DirectExchange directExchange = new DirectExchange(EXCHANGE);
        return new Declarables(queue, directExchange,
                BindingBuilder.bind(queue).to(directExchange).with(ROUTE_KEY));
    }

    // 死信队列
    @Bean
    public Declarables declarablesForDead() {
        Queue queue = new Queue(DEAD_QUEUE);
        DirectExchange directExchange = new DirectExchange(DEAD_EXCHANGE);
        return new Declarables(queue, directExchange,
                BindingBuilder.bind(queue).to(directExchange).with(DEAD_ROUTING_KEY));
    }

    @Bean
    public RetryOperationsInterceptor interceptor() {
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(5)     // 如果报错，那么还会重试4次
                // 发送间隔——第一次：1s  第二次：2s  第三次：4s  第四次：8s
                .backOffOptions(1000, 2.0, 10000)
                // 当达到最大处理次数，发送消息到指定的队列——死信队列
                .recoverer(new RepublishMessageRecoverer(rabbitTemplate, DEAD_EXCHANGE, DEAD_ROUTING_KEY))
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(interceptor());
        // 消费消息的线程数量，默认是1，因此当有两个死信需要处理的时候，是串行的，这个值需要根据消息量调优
        factory.setConcurrentConsumers(10);
        return factory;
    }
}

package com.junyi;

import com.junyi.entity.UserChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @time: 2021/3/11 16:20
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Component
public class UserChangePublish {

    @Autowired
    KafkaTemplate<String, UserChangeEvent> kafkaTemplate;

    public void publish(String topic, UserChangeEvent event) {
        kafkaTemplate.send(topic, event);
    }
}

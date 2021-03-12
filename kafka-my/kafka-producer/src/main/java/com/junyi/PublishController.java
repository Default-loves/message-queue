package com.junyi;

import com.junyi.entity.User;
import com.junyi.entity.UserChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @time: 2021/3/11 16:43
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@RestController
public class PublishController {

    public static final String topic = "jy.user";

    @Autowired
    UserChangePublish userChangePublish;

    @GetMapping("/publish")
    public void publish() {
        User user = User.builder().id(1).name("xiao bai").build();
        UserChangeEvent event = UserChangeEvent.builder()
                .uid(UUID.randomUUID().toString())
                .operation("update")
                .user(user)
                .build();
        userChangePublish.publish(topic, event);
    }
}

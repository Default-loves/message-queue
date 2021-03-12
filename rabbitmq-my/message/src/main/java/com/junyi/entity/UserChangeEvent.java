package com.junyi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @time: 2021/3/11 16:17
 * @version: 1.0
 * @author: junyi Xu
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeEvent implements Serializable {
    private String uid;
    private String operation;
    private User user;
}

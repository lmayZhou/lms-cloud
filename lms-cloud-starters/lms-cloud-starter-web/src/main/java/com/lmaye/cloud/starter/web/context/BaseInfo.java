package com.lmaye.cloud.starter.web.context;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * -- 基础信息
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@Getter
@Setter
@Builder
public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * Token
     */
    private String token;

    /**
     * Token作用域
     */
    private String tokenScope;

    /**
     * 用户
     */
    private Object user;

    /**
     * 日志
     */
    private Object log;
}

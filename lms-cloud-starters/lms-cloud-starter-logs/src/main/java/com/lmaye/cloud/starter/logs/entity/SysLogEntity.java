package com.lmaye.cloud.starter.logs.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- Sys Entity
 * - 系统日志
 *
 * @author lmay.Zhou
 * @date 2020/12/3 13:52
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
public class SysLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 编号
     */
    private Long id;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 终端类型
     */
    private Integer clientType;

    /**
     * 方法
     */
    private String function;

    /**
     * 方法名称
     */
    private String functionName;

    /**
     * 方法路径
     */
    private String path;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 浏览器类型
     */
    private String browserType;

    /**
     * 请求方法(GET/POST/DELETE/PUT)
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 操作时间戳
     */
    private Long operateTimestamp;

    /**
     * 消耗时间(ms)
     */
    private Long consumeTime;

    /**
     * 备注
     */
    private String remark;
}

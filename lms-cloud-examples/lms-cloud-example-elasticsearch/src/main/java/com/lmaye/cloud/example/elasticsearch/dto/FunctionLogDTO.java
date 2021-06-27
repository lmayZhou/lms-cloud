package com.lmaye.cloud.example.elasticsearch.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- FunctionLog Entity
 * - 功能日志
 *
 * @author lmay.Zhou
 * @date 2020/12/3 13:52
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
public class FunctionLogDTO implements Serializable {
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
     * 方法名称
     */
    private String functionName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 消耗时间(ms)
     */
    private Long consumeTime;

    /**
     * 备注
     */
    private String remark;
}

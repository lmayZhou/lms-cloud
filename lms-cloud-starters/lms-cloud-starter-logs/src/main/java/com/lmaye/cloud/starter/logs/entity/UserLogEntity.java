package com.lmaye.cloud.starter.logs.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- UserLog Entity
 * - 用户日志
 *
 * @author lmay.Zhou
 * @date 2020/12/2 12:15
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
public class UserLogEntity implements Serializable {
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
     * 终端类型
     */
    private Integer clientType;

    /**
     * 操作类型
     * - 0.退出; 1.登录;
     */
    private Integer operateType;

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

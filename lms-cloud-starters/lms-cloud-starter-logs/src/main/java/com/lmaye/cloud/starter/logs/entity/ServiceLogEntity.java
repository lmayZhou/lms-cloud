package com.lmaye.cloud.starter.logs.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- ServiceLog Entity
 * - 业务日志
 *
 * @author lmay.Zhou
 * @date 2020/12/3 13:50
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
public class ServiceLogEntity implements Serializable {
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
     * 模块ID
     */
    private String moduleId;

    /**
     * 数据ID
     */
    private String dataId;

    /**
     * 标题
     */
    private String title;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 操作类型
     */
    private String operationType;

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

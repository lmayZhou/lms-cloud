package com.lmaye.cloud.example.elasticsearch.dto;

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
public class ServiceLogDTO implements Serializable {
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
     * 模块ID
     */
    private String moduleId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 标题
     */
    private String title;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * 备注
     */
    private String remark;
}

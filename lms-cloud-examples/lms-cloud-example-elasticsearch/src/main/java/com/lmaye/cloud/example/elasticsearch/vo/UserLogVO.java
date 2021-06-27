package com.lmaye.cloud.example.elasticsearch.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * -- UserLog VO
 *
 * @author lmay.Zhou
 * @date 2020/12/2 12:15
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UserLogVO", description = "用户日志VO")
public class UserLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private Long id;

    /**
     * 应用ID
     */
    @ApiModelProperty("应用ID")
    private String appId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    private String ip;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private Date operateTime;

    /**
     * 消耗时间
     */
    @ApiModelProperty("消耗时间")
    private Long consumeTime;

    /**
     * 滚动ID(深度分页查询)
     */
    @ApiModelProperty("滚动ID(深度分页查询)")
    private String scrollId;
}

package com.lmaye.cloud.starter.web.context;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * -- UserBaseInfo
 * - 用户基本信息
 *
 * @author lmay.Zhou
 * @date 2020/12/22 18:51
 * @email lmay@lmaye.com
 */
@Data
@ApiModel(value = "UserBaseInfo", description = "用户基本信息")
public class UserBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

    /**
     * 用户名
     */
    @JsonProperty("user_name")
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 客户端ID
     */
    @JsonProperty("client_id")
    @ApiModelProperty("客户端ID")
    private String clientId;

    /**
     * 角色
     */
    @ApiModelProperty("角色")
    private List<String> authorities;

    /**
     * 权限范围
     */
    @ApiModelProperty("权限范围")
    private List<String> scope;
}

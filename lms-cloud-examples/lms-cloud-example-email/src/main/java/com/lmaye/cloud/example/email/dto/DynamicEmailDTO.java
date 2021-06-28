package com.lmaye.cloud.example.email.dto;

import com.lmaye.cloud.starter.email.entity.Email;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * -- Dynamic Email
 *
 * @author lmay.Zhou
 * @date 2021/6/28 22:46
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DynamicEmailDTO", description = "邮件信息")
public class DynamicEmailDTO extends Email {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件发送者
     * - 未指定: 随机或默认邮箱账号
     */
    @ApiModelProperty("邮件发送者(未指定: 随机或默认邮箱账号)")
    private String mailSender;
}

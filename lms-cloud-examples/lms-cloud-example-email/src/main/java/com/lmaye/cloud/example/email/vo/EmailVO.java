package com.lmaye.cloud.example.email.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * -- Email
 *
 * @author Lmay Zhou
 * @date 2021/4/14 16:52
 * @email lmay@lmaye.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "EmailVO", description = "邮件信息")
public class EmailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件发送者
     * - 未指定: 随机或默认邮箱账号
     */
    @ApiModelProperty("邮件发送者(未指定: 随机或默认邮箱账号)")
    private String mailSender;

    /**
     * 发件人名称
     * - 未指定: 默认邮箱账号名称
     */
    @ApiModelProperty("发件人名称(未指定: 默认邮箱账号名称)")
    private String senderName;

    /**
     * 邮件接收人
     */
    @ApiModelProperty("邮件接收人")
    private String[] recipient;

    /**
     * 抄送
     */
    @ApiModelProperty("邮件接收人")
    private String[] cc;

    /**
     * 密送
     */
    @ApiModelProperty("邮件接收人")
    private String[] bcc;

    /**
     * 邮件主题
     */
    @ApiModelProperty("邮件接收人")
    private String subject;

    /**
     * 发送邮件格式为 HTML
     */
    @ApiModelProperty("邮件接收人")
    private Boolean html;

    /**
     * 邮件内容
     * <pre>
     *     HTML模版填充示例:
     *
     *     import freemarker.template.Configuration;
     *     ...
     *     FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("ResetPassword.ftl"), GsonUtils.fromJson(data, Map.class))
     * </pre>
     */
    @ApiModelProperty("邮件内容")
    private String content;
}

package com.lmaye.cloud.starter.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发件人名称
     * - 未指定: 默认邮箱账号名称
     */
    private String senderName;

    /**
     * 邮件接收人
     */
    private String[] recipient;

    /**
     * 抄送
     */
    private String[] cc;

    /**
     * 密送
     */
    private String[] bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发送邮件格式为 HTML
     */
    private Boolean html = false;

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
    private String content;
}

package com.lmaye.cloud.starter.email.entity;

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
public class DynamicEmail extends Email {
    private static final long serialVersionUID = 1L;

    /**
     * 邮件发送者
     * - 未指定: 随机或默认邮箱账号
     */
    private String mailSender;
}

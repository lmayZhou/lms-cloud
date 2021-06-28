package com.lmaye.cloud.starter.email.service;

import com.lmaye.cloud.starter.email.entity.Email;

/**
 * -- 邮件发送 Service
 *
 * @author lmay.Zhou
 * @date 2021/5/18 10:32
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public interface EmailSendService {
    /**
     * 发送邮件
     * - Spring Boot 默认配置
     *
     * @param entity 邮件参数
     * @return Boolean
     */
    Boolean sendMail(Email entity);

    /**
     * 发送邮件
     * - 动态发件者
     *
     * @param entity 邮件参数
     * @return Boolean
     */
    Boolean dynamicSendMail(Email entity);
}

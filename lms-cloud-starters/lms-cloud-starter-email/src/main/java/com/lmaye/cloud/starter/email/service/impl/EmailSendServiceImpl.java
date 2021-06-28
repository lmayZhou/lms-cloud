package com.lmaye.cloud.starter.email.service.impl;

import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.HandleException;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.email.EmailAutoConfiguration;
import com.lmaye.cloud.starter.email.entity.Email;
import com.lmaye.cloud.starter.email.service.EmailSendService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

/**
 * -- 邮件发送 Service
 *
 * @author lmay.Zhou
 * @date 2021/5/18 10:31
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Service
@AllArgsConstructor
public class EmailSendServiceImpl implements EmailSendService {
    /**
     * Email Auto Configuration
     */
    @Autowired
    private final EmailAutoConfiguration emailAutoConfiguration;

    /**
     * Java Mail Sender
     */
    private final JavaMailSender javaMailSender;

    /**
     * 发送邮件
     * - Spring Boot 默认配置
     *
     * @param entity 邮件参数
     * @return Boolean
     */
    @Override
    public Boolean sendMail(Email entity) {
        try {
            log.debug("邮件发送数据: {}", GsonUtils.toJson(entity));
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            messageHelper(entity, mimeMessage, ((JavaMailSenderImpl) javaMailSender).getUsername());
            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败: [{}] {}", entity.getRecipient(), e.getMessage());
            throw new HandleException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 发送邮件
     * - 基于Freemarker模板
     *
     * @param entity 邮件参数
     * @return Boolean
     */
    @Override
    public Boolean dynamicSendMail(Email entity) {
        try {
            log.debug("邮件发送数据: {}", GsonUtils.toJson(entity));
            JavaMailSenderImpl mailSender = emailAutoConfiguration.getMailSender(entity.getMailSender());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            messageHelper(entity, mimeMessage, mailSender.getUsername());
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            log.error("邮件发送失败: [{}] {}", entity.getRecipient(), e.getMessage());
            throw new HandleException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 消息处理
     *
     * @param entity      Email
     * @param mimeMessage MimeMessage
     * @param mailSender  邮件发送账户
     * @throws MessagingException 消息异常
     */
    private void messageHelper(Email entity, MimeMessage mimeMessage, String mailSender) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(entity.getSenderName() + "<" + mailSender + ">");
        messageHelper.setTo(entity.getRecipient());
        messageHelper.setSubject(entity.getSubject());
        // 抄送
        String[] cc = entity.getCc();
        if (!Objects.isNull(cc) && cc.length > 0) {
            messageHelper.setCc(cc);
        }
        // 密送
        String[] bcc = entity.getBcc();
        if (!Objects.isNull(bcc) && bcc.length > 0) {
            messageHelper.setBcc(bcc);
        }
        // 邮件内容
        messageHelper.setText(entity.getContent(), entity.getHtml());
    }
}
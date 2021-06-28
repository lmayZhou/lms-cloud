package com.lmaye.cloud.starter.email;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * -- Email Auto Configuration
 *
 * @author Lmay Zhou
 * @date 2021/2/3 15:10
 * @email lmay@lmaye.com
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty(value = "enabled", prefix = "email", matchIfMissing = true)
public class EmailAutoConfiguration {
    /**
     * Email Properties
     */
    @Bean
    EmailProperties emailProperties() {
        return new EmailProperties();
    }

    /**
     * Mail Sender
     */
    private final List<JavaMailSenderImpl> mailSenders = new ArrayList<>();

    /**
     * 初始化 sender
     */
    @PostConstruct
    public void buildMailSender() {
        List<EmailProperties> configs = emailProperties().getConfigs();
        log.info("------------ init Mail Sender ------------");
        configs.forEach(it -> {
            // 邮件发送者
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setDefaultEncoding(it.getDefaultEncoding());
            javaMailSender.setHost(it.getHost());
            javaMailSender.setPort(it.getPort());
            javaMailSender.setProtocol(it.getProtocol());
            javaMailSender.setUsername(it.getUsername());
            javaMailSender.setPassword(it.getPassword());
            // Java Mail Properties
            Properties javaMailProperties = new Properties();
            javaMailProperties.setProperty("mail.transport.protocol", "smtp");
            javaMailProperties.setProperty("mail.smtp.auth", "true");
            javaMailProperties.setProperty("mail.smtp.ssl.enable", "true");
            javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");
            javaMailProperties.setProperty("mail.smtp.starttls.required", "true");
            javaMailSender.setJavaMailProperties(javaMailProperties);
            mailSenders.add(javaMailSender);
        });
    }

    /**
     * 获取MailSender
     *
     * @return JavaMailSenderImpl
     */
    public JavaMailSenderImpl javaMailSender() {
        // 随机返回JavaMailSender
        return mailSenders.get(new Random().nextInt(mailSenders.size()));
    }

    /**
     * 获取 JavaMailSender
     *
     * @param username 邮件账号
     * @return JavaMailSenderImpl
     */
    public JavaMailSenderImpl getMailSender(String username) {
        if (CollectionUtils.isEmpty(mailSenders)) {
            buildMailSender();
        }
        return StringUtils.isBlank(username) ? javaMailSender() : mailSenders.stream().filter(it ->
                Objects.equals(it.getUsername(), username)).findFirst().orElse(mailSenders.get(0));
    }

    /**
     * 清理 sender
     */
    public void clear() {
        log.debug("------------ Clear Mail Sender: [{}] ------------", mailSenders.size());
        mailSenders.clear();
    }
}

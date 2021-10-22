package com.lmaye.cloud.starter.drools;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * -- Drools Auto Configuration
 *
 * @author Lmay Zhou
 * @date 2021/10/22 11:04
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
@EnableConfigurationProperties(DroolsProperties.class)
@ConditionalOnProperty(value = "enabled", prefix = "drools", matchIfMissing = true)
public class DroolsAutoConfiguration {
    /**
     * Kie Services
     */
    private final KieServices kieServices = KieServices.Factory.get();

    /**
     * 规则引擎配置
     *
     * @return DroolsProperties
     */
    @Bean
    DroolsProperties droolsProperties() {
        return new DroolsProperties();
    }

    /**
     * Kie Base
     *
     * @return KieBase
     * @throws IOException IO异常
     */
    @Bean
    KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    /**
     * Kie Session
     * - 会话
     *
     * @return KieSession
     * @throws IOException IO异常
     */
    @Bean
    @ConditionalOnMissingBean
    KieSession kieSession() throws IOException {
        return kieContainer().newKieSession();
    }

    /**
     * Kie File System
     * - 文件系统
     *
     * @return KieFileSystem
     * @throws IOException IO异常
     */
    @Bean
    KieFileSystem kieFileSystem() throws IOException {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        for (Resource file : getRuleFiles()) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(
                    droolsProperties().getRulesPath() + file.getFilename(), "UTF-8"));
        }
        return kieFileSystem;
    }

    /**
     * Kie Container
     * - 容器
     *
     * @return KieContainer
     * @throws IOException IO异常
     */
    @Bean
    KieContainer kieContainer() throws IOException {
        KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();
        return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
    }

    /**
     * 获取规则文件
     *
     * @return Resource[]
     * @throws IOException IO异常
     */
    private Resource[] getRuleFiles() throws IOException {
        return new PathMatchingResourcePatternResolver().getResources(
                "classpath*:" + droolsProperties().getRulesPath() + "**/*.*");
    }
}

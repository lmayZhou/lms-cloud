package com.lmaye.cloud.starter.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * -- Docket Bean Factory Post Processor
 *
 * @author Lmay Zhou
 * @date 2021/11/4 11:40
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class DocketBeanFactoryPostProcessor implements BeanFactoryPostProcessor, BeanFactoryAware {
    /**
     * Bean Factory
     */
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 获取 documentationPluginRegistry Bean，强制依赖 swagger-spring-boot-starter 配置 Bean 初始化
     *
     * @param beanFactory ConfigurableListableBeanFactory
     * @throws BeansException 异常
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) beanFactory;
        // 获取 documentationPluginRegistry Bean，强制依赖 swagger-spring-boot-starter 配置 Bean 初始化
        String beanName = "documentationPluginRegistry";
        if (beanRegistry.containsBeanDefinition(beanName)) {
            BeanDefinition documentationPluginRegistryBeanDefinition = beanRegistry.getBeanDefinition(beanName);
            documentationPluginRegistryBeanDefinition.setDependsOn("createSpringFoxRestApi");
        }
    }
}

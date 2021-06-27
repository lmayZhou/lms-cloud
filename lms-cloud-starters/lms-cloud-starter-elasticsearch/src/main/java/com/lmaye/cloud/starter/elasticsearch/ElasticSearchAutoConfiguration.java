package com.lmaye.cloud.starter.elasticsearch;

import com.lmaye.cloud.starter.elasticsearch.repository.IElasticSearchRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientProperties;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * -- Elasticsearch Configuration
 *
 * @author lmay.Zhou
 * @date 2020/12/1 13:44
 * @email lmay@lmaye.com
 */
@Configuration
@ConditionalOnBean({ElasticsearchRestClientProperties.class, ReactiveElasticsearchRestClientProperties.class})
@AutoConfigureAfter(ElasticsearchDataAutoConfiguration.class)
@EnableElasticsearchRepositories(basePackages = "com.lmaye", basePackageClasses = IElasticSearchRepository.class)
public class ElasticSearchAutoConfiguration {

}

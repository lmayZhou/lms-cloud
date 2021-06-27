package com.lmaye.cloud.starter.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * -- Elasticsearch Repository Interface
 *
 * @author lmay.Zhou
 * @date 2020/12/1 14:08
 * @email lmay@lmaye.com
 */
@NoRepositoryBean
public interface IElasticSearchRepository<T, ID extends Serializable> extends ElasticsearchRepository<T, ID> {
}

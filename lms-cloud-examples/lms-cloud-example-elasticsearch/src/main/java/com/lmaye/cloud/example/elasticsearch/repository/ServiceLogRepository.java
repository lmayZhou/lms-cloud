package com.lmaye.cloud.example.elasticsearch.repository;

import com.lmaye.cloud.example.elasticsearch.entity.ServiceLogEntity;
import com.lmaye.cloud.starter.elasticsearch.repository.IElasticSearchRepository;
import org.springframework.stereotype.Repository;

/**
 * -- ServiceLog Repository
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:10
 * @email lmay@lmaye.com
 */
@Repository
public interface ServiceLogRepository extends IElasticSearchRepository<ServiceLogEntity, Long> {
}

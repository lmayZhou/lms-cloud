package com.lmaye.cloud.example.repository;

import com.lmaye.cloud.starter.elasticsearch.repository.IElasticSearchRepository;
import com.lmaye.cloud.starter.logs.entity.FunctionLogEntity;
import org.springframework.stereotype.Repository;

/**
 * -- FunctionLog Repository
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:16
 * @email lmay@lmaye.com
 */
@Repository
public interface FunctionLogRepository extends IElasticSearchRepository<FunctionLogEntity, Long> {
}

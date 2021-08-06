package com.lmaye.cloud.example.repository;

import com.lmaye.cloud.starter.logs.entity.UserLogEntity;
import com.lmaye.cloud.starter.elasticsearch.repository.IElasticSearchRepository;
import org.springframework.stereotype.Repository;

/**
 * -- UserLog Repository
 *
 * @author lmay.Zhou
 * @date 2020/12/2 12:26
 * @email lmay@lmaye.com
 */
@Repository
public interface UserLogRepository extends IElasticSearchRepository<UserLogEntity, Long> {
}

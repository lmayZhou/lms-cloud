package com.lmaye.cloud.example.elasticsearch.service.impl;

import com.lmaye.cloud.example.elasticsearch.entity.UserLogEntity;
import com.lmaye.cloud.example.elasticsearch.repository.UserLogRepository;
import com.lmaye.cloud.example.elasticsearch.service.IUserLogService;
import com.lmaye.cloud.starter.elasticsearch.service.impl.ElasticSearchServiceImpl;
import org.springframework.stereotype.Service;

/**
 * -- UserLog Service Impl
 *
 * @author lmay.Zhou
 * @date 2020/12/2 12:29
 * @email lmay@lmaye.com
 */
@Service
public class UserLogServiceImpl extends ElasticSearchServiceImpl<UserLogRepository, UserLogEntity, Long>
        implements IUserLogService {
    public UserLogServiceImpl(UserLogRepository repository) {
        super(repository);
    }
}

package com.lmaye.cloud.example.service.impl;

import com.lmaye.cloud.starter.logs.entity.UserLogEntity;
import com.lmaye.cloud.example.repository.UserLogRepository;
import com.lmaye.cloud.example.service.IUserLogService;
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

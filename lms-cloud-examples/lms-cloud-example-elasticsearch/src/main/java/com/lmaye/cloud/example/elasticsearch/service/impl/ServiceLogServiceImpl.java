package com.lmaye.cloud.example.elasticsearch.service.impl;

import com.lmaye.cloud.example.elasticsearch.entity.ServiceLogEntity;
import com.lmaye.cloud.example.elasticsearch.repository.ServiceLogRepository;
import com.lmaye.cloud.example.elasticsearch.service.IServiceLogService;
import com.lmaye.cloud.starter.elasticsearch.service.impl.ElasticSearchServiceImpl;
import org.springframework.stereotype.Service;

/**
 * -- ServiceLog Service Impl
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:20
 * @email lmay@lmaye.com
 */
@Service
public class ServiceLogServiceImpl extends ElasticSearchServiceImpl<ServiceLogRepository, ServiceLogEntity, Long>
        implements IServiceLogService {
    public ServiceLogServiceImpl(ServiceLogRepository repository) {
        super(repository);
    }
}

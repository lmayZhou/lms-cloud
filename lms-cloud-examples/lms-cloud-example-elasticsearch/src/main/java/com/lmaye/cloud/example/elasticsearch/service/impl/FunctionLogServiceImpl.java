package com.lmaye.cloud.example.elasticsearch.service.impl;

import com.lmaye.cloud.example.elasticsearch.entity.FunctionLogEntity;
import com.lmaye.cloud.example.elasticsearch.repository.FunctionLogRepository;
import com.lmaye.cloud.example.elasticsearch.service.IFunctionLogService;
import com.lmaye.cloud.starter.elasticsearch.service.impl.ElasticSearchServiceImpl;
import org.springframework.stereotype.Service;

/**
 * -- FunctionLog Service Impl
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:21
 * @email lmay@lmaye.com
 */
@Service
public class FunctionLogServiceImpl extends ElasticSearchServiceImpl<FunctionLogRepository, FunctionLogEntity, Long>
        implements IFunctionLogService {
    public FunctionLogServiceImpl(FunctionLogRepository repository) {
        super(repository);
    }
}

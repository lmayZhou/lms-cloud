package com.lmaye.cloud.example.elasticsearch.service;

import com.lmaye.cloud.example.elasticsearch.entity.ServiceLogEntity;
import com.lmaye.cloud.starter.elasticsearch.service.IElasticSearchService;

/**
 * -- ServiceLog Service
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:18
 * @email lmay@lmaye.com
 */
public interface IServiceLogService extends IElasticSearchService<ServiceLogEntity, Long> {
}

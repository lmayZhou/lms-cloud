package com.lmaye.cloud.example.elasticsearch.service;

import com.lmaye.cloud.example.elasticsearch.entity.FunctionLogEntity;
import com.lmaye.cloud.starter.elasticsearch.service.IElasticSearchService;

/**
 * -- FunctionLog Service
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:18
 * @email lmay@lmaye.com
 */
public interface IFunctionLogService extends IElasticSearchService<FunctionLogEntity, Long> {
}

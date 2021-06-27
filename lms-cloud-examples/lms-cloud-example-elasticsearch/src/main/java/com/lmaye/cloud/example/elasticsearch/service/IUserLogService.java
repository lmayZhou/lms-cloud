package com.lmaye.cloud.example.elasticsearch.service;


import com.lmaye.cloud.example.elasticsearch.entity.UserLogEntity;
import com.lmaye.cloud.starter.elasticsearch.service.IElasticSearchService;

/**
 * -- UserLog Service
 *
 * @author lmay.Zhou
 * @date 2020/12/2 12:28
 * @email lmay@lmaye.com
 */
public interface IUserLogService extends IElasticSearchService<UserLogEntity, Long> {
}

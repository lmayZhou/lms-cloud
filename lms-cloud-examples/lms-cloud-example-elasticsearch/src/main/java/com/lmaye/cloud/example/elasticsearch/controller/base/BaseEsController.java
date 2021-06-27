package com.lmaye.cloud.example.elasticsearch.controller.base;

import com.lmaye.cloud.example.elasticsearch.annotation.EsLog;
import com.lmaye.cloud.starter.elasticsearch.service.IElasticSearchService;
import com.lmaye.cloud.starter.web.context.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * -- Base Es Controller
 *
 * @author lmay.Zhou
 * @date 2020/12/3 17:13
 * @email lmay@lmaye.com
 */
public abstract class BaseEsController<S extends IElasticSearchService<T, ID>, T extends Serializable,
        ID extends Serializable> {
    /**
     * Service
     */
    protected S service;

    public BaseEsController(S service) {
        this.service = service;
    }

    /**
     * 新增日志
     *
     * @param param 请求参数
     * @return ResultVO<UserLogEntity>
     */
    @PostMapping("/save")
    @ApiOperation("新增日志")
    @EsLog(indexNames = {"user_logs", "function_logs", "service_logs"}, appId = "ES00001", moduleId = "M00001", title = "新增ES日志")
    public ResultVO<T> save(@RequestBody @Valid T param) {
        return ResultVO.success(service.insertOrUpdate(param));
    }
}

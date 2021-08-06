package com.lmaye.cloud.example.controller;

import com.lmaye.cloud.example.controller.base.BaseEsController;
import com.lmaye.cloud.example.service.IFunctionLogService;
import com.lmaye.cloud.starter.logs.annotation.FunctionLog;
import com.lmaye.cloud.starter.logs.entity.FunctionLogEntity;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * -- FunctionLog Controller
 *
 * @author lmay.Zhou
 * @date 2020/12/3 14:08
 * @email lmay@lmaye.com
 */
@RestController
@RequestMapping("/es/log/function")
@Api(tags = "功能日志ES相关接口")
@FunctionLog(appId = "LS10001", moduleId = "FunctionLog", logType = "0", desc = "全局日志")
public class FunctionLogController extends BaseEsController<IFunctionLogService, FunctionLogEntity, Long> {
    public FunctionLogController(IFunctionLogService service) {
        super(service);
    }

    /**
     * 查询日志All
     *
     * @param query 查询参数
     * @return ResultVO<List < FunctionLogEntity>>
     */
    @PostMapping("/searchAll")
    @ApiOperation("查询日志All")
    public ResultVO<List<FunctionLogEntity>> searchAll(@RequestBody ListQuery query) {
        return ResultVO.success(service.findAll(query, FunctionLogEntity.class));
    }

    /**
     * 查询日志All
     * - 深度
     *
     * @param query 查询参数
     * @return ResultVO<List < FunctionLogEntity>>
     */
    @PostMapping("/searchScrollAll")
    @ApiOperation("查询日志All(深度)")
    public ResultVO<List<FunctionLogEntity>> searchScrollAll(@RequestBody ListQuery query) {
        return ResultVO.success(service.findScrollAll(query, FunctionLogEntity.class));
    }

    /**
     * 分页查询日志
     *
     * @param query 查询参数
     * @return ResultVO<PageResult < FunctionLogEntity>>
     */
    @PostMapping("/searchPage")
    @ApiOperation("分页查询日志")
    public ResultVO<PageResult<FunctionLogEntity>> searchPage(@RequestBody PageQuery query) {
        return ResultVO.success(service.findPage(query, FunctionLogEntity.class));
    }

    /**
     * 分页查询日志
     * - 深度(不支持跨页)
     *
     * @param query 查询参数
     * @return ResultVO<PageResult < FunctionLogEntity>>
     */
    @PostMapping("/searchScrollPage")
    @ApiOperation(value = "分页查询日志(深度)", notes = "不支持跨页")
    public ResultVO<PageResult<FunctionLogEntity>> searchScrollPage(@RequestBody PageQuery query) {
        return ResultVO.success(service.findScrollPage(query, FunctionLogEntity.class));
    }

    /**
     * 查询日志总数
     *
     * @param query 查询参数
     * @return ResultVO<Long>
     */
    @PostMapping("/count")
    @ApiOperation("查询日志总数")
    public ResultVO<Long> count(@RequestBody Query query) {
        return ResultVO.success(service.count(query, FunctionLogEntity.class));
    }
}

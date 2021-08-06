package com.lmaye.cloud.example.controller;

import com.lmaye.cloud.example.controller.base.BaseEsController;
import com.lmaye.cloud.example.service.IUserLogService;
import com.lmaye.cloud.starter.logs.annotation.UserLog;
import com.lmaye.cloud.starter.logs.entity.UserLogEntity;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * -- ElasticSearch Controller
 *
 * @author lmay.Zhou
 * @date 2020/12/2 13:41
 * @email lmay@lmaye.com
 */
@Slf4j
@RestController
@RequestMapping("/es/log/user")
@Api(tags = "用户日志ES相关接口")
public class UserLogEsController extends BaseEsController<IUserLogService, UserLogEntity, Long> {
    public UserLogEsController(IUserLogService service) {
        super(service);
    }

    /**
     * 查询日志All
     *
     * @param query 查询参数
     * @return ResultVO<List < UserLogEntity>>
     */
    @PostMapping("/searchAll")
    @ApiOperation("查询日志All")
    public ResultVO<List<UserLogEntity>> searchAll(@RequestBody ListQuery query) {
        return ResultVO.success(service.findAll(query, UserLogEntity.class));
    }

    /**
     * 查询日志All
     * - 深度
     *
     * @param query 查询参数
     * @return ResultVO<List < UserLogEntity>>
     */
    @PostMapping("/searchScrollAll")
    @ApiOperation("查询日志All(深度)")
    public ResultVO<List<UserLogEntity>> searchScrollAll(@RequestBody ListQuery query) {
        return ResultVO.success(service.findScrollAll(query, UserLogEntity.class));
    }

    /**
     * 分页查询日志
     *
     * @param query 查询参数
     * @return ResultVO<PageResult < UserLogEntity>>
     */
    @PostMapping("/searchPage")
    @ApiOperation("分页查询日志")
    public ResultVO<PageResult<UserLogEntity>> searchPage(@RequestBody PageQuery query) {
        return ResultVO.success(service.findPage(query, UserLogEntity.class));
    }

    /**
     * 分页查询日志
     * - 深度(不支持跨页)
     *
     * @param query 查询参数
     * @return ResultVO<PageResult < UserLogEntity>>
     */
    @PostMapping("/searchScrollPage")
    @ApiOperation(value = "分页查询日志(深度)", notes = "不支持跨页")
    public ResultVO<PageResult<UserLogEntity>> searchScrollPage(@RequestBody PageQuery query) {
        return ResultVO.success(service.findScrollPage(query, UserLogEntity.class));
    }

    /**
     * 查询日志总数
     *
     * @param query 查询参数
     * @return ResultVO<Long>
     */
    @PostMapping("/count")
    @ApiOperation("查询日志总数")
    @UserLog(appId = "LS10001", operateType = 0, desc = "查询日志总数")
    public ResultVO<Long> count(@RequestBody Query query) {
        return ResultVO.success(service.count(query, UserLogEntity.class));
    }
}

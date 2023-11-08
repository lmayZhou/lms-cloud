package com.lmaye.cloud.starter.web.controller;

import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.service.IAppService;
import com.lmaye.cloud.starter.web.service.IRestConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * -- BaseController
 * - 基础的数据增删改查相关接口
 *
 * @param <S>  Service
 * @param <C>  Converter
 * @param <T>  Entity
 * @param <V>  VO
 * @param <D>  DTO
 * @param <ID> ID
 * @author lmay.Zhou
 * @date 2021/7/1 22:50
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public abstract class BaseController<S extends IAppService<T, ID>, C extends IRestConverter<T, V, D>,
        T extends Serializable, V extends Serializable, D extends Serializable, ID extends Serializable> {
    /**
     * IService
     */
    @Autowired
    protected S service;

    /**
     * IRestConverter
     */
    @Autowired
    protected C restConverter;

    /**
     * 编辑
     *
     * @param param 请求参数
     * @return ResultVO<V>
     */
    @PostMapping("/edit")
    @Operation(summary = "编辑", description = "新增/修改")
    public ResultVO<V> edit(@RequestBody @Validated D param) {
        return ResultVO.success(restConverter.convertEntityToVo(service.insertOrUpdate(restConverter.convertDtoToEntity(param))));
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return ResultVO<Boolean>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除", description = "根据主键ID",
            parameters = @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.PATH))
    public ResultVO<Boolean> delete(@PathVariable ID id) {
        return ResultVO.success(service.deleteById(id));
    }

    /**
     * 批量删除
     *
     * @param ids 主键ID
     * @return ResultVO<Boolean>
     */
    @DeleteMapping("/batch/{ids}")
    @Operation(summary = "批量删除", description = "根据主键ID",
            parameters = @Parameter(name = "ids", description = "主键ID", required = true, in = ParameterIn.PATH))
    public ResultVO<Boolean> delete(@PathVariable List<ID> ids) {
        return ResultVO.success(service.deleteByIds(ids));
    }

    /**
     * 查询
     * - 根据主键ID
     *
     * @param id 主键ID
     * @return ResultVO<V>
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询", description = "根据主键ID",
            parameters = @Parameter(name = "id", description = "主键ID", required = true, in = ParameterIn.PATH))
    public ResultVO<V> queryById(@PathVariable ID id) {
        return ResultVO.success(restConverter.convertEntityToVo(service.findById(id)));
    }

    /**
     * 查询列表
     *
     * @param query 请求参数
     * @return ResultVO<List < V>>
     */
    @PostMapping("/queryRecords")
    @Operation(summary = "查询列表", description = "根据条件查询所有数据")
    public ResultVO<List<V>> queryRecords(@RequestBody ListQuery query) {
        return ResultVO.success(restConverter.convertEntityToVoList(service.findAll(query)));
    }

    /**
     * 分页查询
     *
     * @param query 请求参数
     * @return ResultVO<PageResult < V>>
     */
    @PostMapping("/queryPage")
    @Operation(summary = "分页查询", description = "根据条件查询分页数据")
    public ResultVO<PageResult<V>> queryPage(@RequestBody PageQuery query) {
        return ResultVO.success(restConverter.convertEntityToVoPage(service.findPage(query)));
    }
}

package com.lmaye.cloud.starter.web.controller;

import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.service.IAppService;
import com.lmaye.cloud.starter.web.service.IRestConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * -- Base Controller
 *
 * @param <S>  Service
 * @param <C>  Converter
 * @param <T>  Entity
 * @param <V>  VO
 * @param <D>  DTO
 * @param <ID> ID
 *
 * @author lmay.Zhou
 * @date 2021/7/1 22:50
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@AllArgsConstructor
public abstract class BaseController<S extends IAppService<T, ID>, C extends IRestConverter<T, V, D>,
        T extends Serializable, V extends Serializable, D extends Serializable, ID extends Serializable> {
    /**
     * IService
     */
    protected final S service;

    /**
     * IRestConverter
     */
    protected final C restConverter;

    /**
     * 新增
     *
     * @param param 请求参数
     * @return ResultVO<V>
     */
    @PostMapping("/add")
    @ApiOperation("新增")
    public ResultVO<V> add(@RequestBody @Validated D param) {
        return service.insert(restConverter.convertDtoToEntity(param)).map(it ->
                ResultVO.success(restConverter.convertEntityToVo(it))).orElseGet(() -> ResultVO.success(null));
    }

    /**
     * 编辑
     *
     * @param param 请求参数
     * @return ResultVO<V>
     */
    @PostMapping("/edit")
    @ApiOperation("编辑")
    public ResultVO<V> edit(@RequestBody @Validated D param) {
        return service.update(restConverter.convertDtoToEntity(param)).map(it ->
                ResultVO.success(restConverter.convertEntityToVo(it))).orElseGet(() -> ResultVO.success(null));
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return ResultVO<Boolean>
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除")
    public ResultVO<Boolean> delete(@PathVariable @ApiParam(value = "主键ID", required = true) ID id) {
        return ResultVO.success(service.deleteById(id));
    }

    /**
     * 查询
     * - 根据主键ID
     *
     * @param id 主键ID
     * @return ResultVO<V>
     */
    @GetMapping("/{id}")
    @ApiOperation("查询")
    public ResultVO<V> queryById(@PathVariable @ApiParam(value = "主键ID", required = true) ID id) {
        return service.findById(id).map(it -> ResultVO.success(restConverter.convertEntityToVo(it))).orElseGet(()
                -> ResultVO.success(null));
    }

    /**
     * 查询列表
     *
     * @param query 请求参数
     * @return ResultVO<List<V>>
     */
    @PostMapping("/queryRecords")
    @ApiOperation("查询列表")
    public ResultVO<List<V>> queryRecords(@RequestBody ListQuery query) {
        return ResultVO.success(service.findAll(query).stream().map(restConverter::convertEntityToVo).collect(Collectors.toList()));
    }

    /**
     * 分页查询
     *
     * @param query 请求参数
     * @return ResultVO<List<V>>
     */
    @PostMapping("/queryPage")
    @ApiOperation("分页查询")
    public ResultVO<PageResult<V>> queryPage(@RequestBody PageQuery query) {
        PageResult<V> result = new PageResult<>();
        PageResult<T> pageResult = service.findPage(query);
        result.setRecords(pageResult.getRecords().stream().map(restConverter::convertEntityToVo).collect(Collectors.toList()));
        result.setPageIndex(pageResult.getPageIndex());
        result.setPageSize(pageResult.getPageSize());
        result.setPages(pageResult.getPages());
        result.setTotal(pageResult.getTotal());
        return ResultVO.success(result);
    }
}
package com.lmaye.cloud.starter.web.controller;

import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.service.IAppService;
import com.lmaye.cloud.starter.web.service.IRestConverter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

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
        T extends Serializable, V extends Serializable, D extends Serializable, ID extends Serializable>
        implements IRestConverter<T, V, D> {
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
        return service.insert(restConverter.convertDtoToEntity(param)).map(t ->
                ResultVO.success(restConverter.convertEntityToVo(t))).orElseGet(() -> ResultVO.success(null));
    }

    /**
     * 编辑
     *
     * @param param 请求参数
     * @return ResultVO<V>
     */
    @PostMapping("/edit")
    @ApiOperation("编辑")
    public ResultVO<V> edit(@RequestBody D param) {
        return service.update(restConverter.convertDtoToEntity(param)).map(t ->
                ResultVO.success(restConverter.convertEntityToVo(t))).orElseGet(() -> ResultVO.success(null));
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
    public ResultVO<V> query(@PathVariable @ApiParam(value = "主键ID", required = true) ID id) {
        return service.findById(id).map(t -> ResultVO.success(restConverter.convertEntityToVo(t))).orElseGet(()
                -> ResultVO.success(null));
    }
}

package com.lmaye.cloud.starter.web.service;

import com.lmaye.cloud.starter.web.context.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * -- Rest Converter 对象转换器
 *
 * @author lmay.Zhou
 * @date 2021/6/10 21:40
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public interface IRestConverter<T extends Serializable, V extends Serializable, D extends Serializable> {
    /**
     * DTO 转 Entity
     *
     * @param dto D
     * @return T
     */
    T convertDtoToEntity(D dto);

    /**
     * DTO 转 Entity
     * - List
     *
     * @param records List<D>
     * @return List<T>
     */
    List<T> convertDtoToEntityList(List<D> records);

    /**
     * DTO 转 VO
     *
     * @param dto D
     * @return V
     */
    V convertDtoToVo(D dto);

    /**
     * DTO 转 VO
     * - List
     *
     * @param records List<D>
     * @return List<V>
     */
    List<V> convertDtoToVoList(List<D> records);

    /**
     * Entity 转 VO
     *
     * @param entity T
     * @return V
     */
    V convertEntityToVo(T entity);

    /**
     * Entity 转 VO
     * - List
     *
     * @param records List<T>
     * @return List<V>
     */
    List<V> convertEntityToVoList(List<T> records);

    /**
     * Entity 转 VO
     * - Page
     *
     * @param records PageResult<T>
     * @return PageResult<V>
     */
    PageResult<V> convertEntityToVoPage(PageResult<T> records);

    /**
     * Entity 转 DTO
     *
     * @param entity T
     * @return D
     */
    D convertEntityToDto(T entity);

    /**
     * Entity 转 DTO
     * - List
     *
     * @param records List<T>
     * @return List<D>
     */
    List<D> convertEntityToDtoList(List<T> records);

    /**
     * VO 转 DTO
     *
     * @param vo V
     * @return D
     */
    D convertVoToDto(V vo);

    /**
     * VO 转 DTO
     * - List
     *
     * @param records List<V>
     * @return List<D>
     */
    List<D> convertVoToDtoList(List<V> records);
}

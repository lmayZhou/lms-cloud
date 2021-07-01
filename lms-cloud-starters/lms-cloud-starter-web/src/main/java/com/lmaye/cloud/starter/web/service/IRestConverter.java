package com.lmaye.cloud.starter.web.service;

import java.io.Serializable;

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
     * DTO 转 VO
     *
     * @param dto D
     * @return V
     */
    V convertDtoToVo(D dto);

    /**
     * Entity 转 VO
     *
     * @param entity T
     * @return V
     */
    V convertEntityToVo(T entity);

    /**
     * Entity 转 DTO
     *
     * @param entity T
     * @return D
     */
    D convertEntityToDto(T entity);

    /**
     * VO 转 DTO
     *
     * @param vo V
     * @return D
     */
    D convertVoToDto(V vo);
}

package com.lmaye.cloud.starter.web.service;

import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * -- Service
 *
 * @author lmay.Zhou
 * @date 2020/1/2 14:45 星期四
 * @email lmay@lmaye.com
 */
public interface IAppService<T, ID extends Serializable> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     * @throws ServiceException operate exception
     */
    <S extends T> S insertOrUpdate(S entity) throws ServiceException;

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     * @throws ServiceException         operate exception
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities) throws ServiceException;

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     * @throws ServiceException         operate exception
     */
    void deleteById(ID id) throws ServiceException;

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     * @throws ServiceException operate exception
     */
    Optional<T> findById(ID id) throws ServiceException;

    /**
     * Returns all entities matching the given {@link Query}. In case no match could be found an empty {@link List}
     * is returned.
     *
     * @param query must not be {@literal null}.
     * @return a {@link List} of entities matching the given {@link Query}.
     * @throws ServiceException operate exception
     */
    List<T> findAll(Query query) throws ServiceException;

    /**
     * Returns a {@link List} of entities matching the given {@link Query}.In case no match could be found, an empty
     * {@link List} is returned.
     *
     * @param query may be {@literal null}.
     * @return a {@link List} of entities matching the given {@link Query}.
     * @throws ServiceException operate exception
     */
    List<T> findAll(ListQuery query) throws ServiceException;

    /**
     * Returns a {@link PageResult} of entities matching the given {@link Query}.In case no match could be found, an empty
     * {@link PageResult} is returned.
     *
     * @param query must not be {@literal null}.
     * @return a {@link PageResult} of entities matching the given {@link Query}.
     * @throws ServiceException operate exception
     */
    PageResult<T> findAll(PageQuery query) throws ServiceException;

    /**
     * Returns entities number matching the given {@link Query}.In case no match could be found, an zero
     *
     * @param query must not be {@literal null}.
     * @return a number
     * @throws ServiceException operate exception
     */
    long count(Query query) throws ServiceException;
}

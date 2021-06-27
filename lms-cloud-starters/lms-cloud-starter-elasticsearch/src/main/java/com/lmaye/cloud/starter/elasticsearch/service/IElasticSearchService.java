package com.lmaye.cloud.starter.elasticsearch.service;

import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * -- ElasticSearch Service
 *
 * @author lmay.Zhou
 * @date 2020/12/1 15:29
 * @email lmay@lmaye.com
 */
public interface IElasticSearchService<T, ID extends Serializable> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    <S extends T> S insertOrUpdate(S entity);

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void deleteById(ID id);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    Optional<T> findById(ID id);

    /**
     * Returns all entities matching the given {@link Query}. In case no match could be found an empty {@link List}
     * is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link Query}.
     */
    List<T> findAll(Query query, Class<T> clazz);

    /**
     * Returns a {@link List} of entities matching the given {@link ListQuery}.In case no match could be found, an empty
     * {@link List} is returned.
     *
     * @param query may be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link ListQuery}.
     */
    List<T> findAll(ListQuery query, Class<T> clazz);

    /**
     * Returns a {@link List} of entities matching the given {@link ListQuery}.In case no match could be found, an empty
     * {@link List} is returned.
     *
     * @param query may be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link ListQuery}.
     */
    List<T> findScrollAll(ListQuery query, Class<T> clazz);

    /**
     * Returns a {@link PageResult} of entities matching the given {@link PageQuery}.In case no match could be found, an empty
     * {@link PageResult} is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link PageResult} of entities matching the given {@link PageQuery}.
     */
    PageResult<T> findPage(PageQuery query, Class<T> clazz);

    /**
     * Returns a {@link PageResult} of entities matching the given {@link PageQuery}.In case no match could be found, an empty
     * {@link PageResult} is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link PageResult} of entities matching the given {@link PageQuery}.
     */
    PageResult<T> findScrollPage(PageQuery query, Class<T> clazz);

    /**
     * Returns entities number matching the given {@link Query}.In case no match could be found, an zero
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a number
     */
    long count(Query query, Class<T> clazz);
}

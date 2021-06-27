package com.lmaye.cloud.starter.elasticsearch.service.impl;

import com.google.common.collect.Lists;
import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.elasticsearch.repository.IElasticSearchRepository;
import com.lmaye.cloud.starter.elasticsearch.service.IElasticSearchService;
import com.lmaye.cloud.starter.elasticsearch.utils.ElasticSearchUtil;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * -- ElasticSearch Service Impl
 *
 * @author lmay.Zhou
 * @date 2020/12/1 15:56
 * @email lmay@lmaye.com
 */
@Slf4j
public class ElasticSearchServiceImpl<R extends IElasticSearchRepository<T, ID>, T, ID extends Serializable>
        implements IElasticSearchService<T, ID> {
    /**
     * Repository
     */
    private final R repository;

    /**
     * ElasticsearchOperations
     */
    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    /**
     * RestHighLevelClient
     */
    @Resource
    private RestHighLevelClient restHighLevelClient;

    public ElasticSearchServiceImpl(R repository) {
        this.repository = repository;
    }

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     *
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    @Override
    public <S extends T> S insertOrUpdate(S entity) {
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Saves all given entities.
     *
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        try {
            return repository.saveAll(entities);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    @Override
    public void deleteById(ID id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return the entity with the given id or {@literal Optional#empty()} if none found
     */
    @Override
    public Optional<T> findById(ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns all entities matching the given {@link Query}. In case no match could be found an empty {@link List}
     * is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link Query}.
     */
    @Override
    public List<T> findAll(Query query, Class<T> clazz) {
        try {
            if (Objects.isNull(query)) {
                return Lists.newArrayList(repository.findAll());
            }
            NativeSearchQuery searchQuery = new NativeSearchQuery(ElasticSearchUtil.convert(query));
            // es默认10条
            searchQuery.setMaxResults(1000);
            SearchHits<T> searchHits;
            Document doc = clazz.getAnnotation(Document.class);
            if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
                searchHits = elasticsearchOperations.search(searchQuery, clazz, IndexCoordinates.of(doc.indexName()));
            } else {
                searchHits = elasticsearchOperations.search(searchQuery, clazz);
            }
            return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns a {@link List} of entities matching the given {@link ListQuery}.In case no match could be found, an empty
     * {@link List} is returned.
     *
     * @param query may be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link ListQuery}.
     */
    @Override
    public List<T> findAll(ListQuery query, Class<T> clazz) {
        try {
            if (Objects.isNull(query) || Objects.isNull(query.getQuery())) {
                return Lists.newArrayList(repository.findAll());
            }
            NativeSearchQuery searchQuery = new NativeSearchQuery(ElasticSearchUtil.convert(query.getQuery()));
            // 排序
            if (!Objects.isNull(query.getSort()) && !CollectionUtils.isEmpty(query.getSort().getOrder())) {
                query.getSort().getOrder().forEach(it -> {
                    try {
                        String name = it.getName();
                        if (Objects.equals(clazz.getDeclaredField(name).getType(), String.class)) {
                            name = it.getName() + ".keyword";
                        }
                        searchQuery.addSort(Sort.by(Objects.equals(1, it.getAsc()) ? Sort.Order.asc(name)
                                : Sort.Order.desc(name)));
                    } catch (Exception e) {
                        log.error("排序处理异常: ", e);
                    }
                });
            }
            // es默认10条
            searchQuery.setMaxResults(1000);
            SearchHits<T> searchHits;
            Document doc = clazz.getAnnotation(Document.class);
            if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
                searchHits = elasticsearchOperations.search(searchQuery, clazz, IndexCoordinates.of(doc.indexName()));
            } else {
                searchHits = elasticsearchOperations.search(searchQuery, clazz);
            }
            return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns a {@link List} of entities matching the given {@link ListQuery}.In case no match could be found, an empty
     * {@link List} is returned.
     *
     * @param query may be {@literal null}.
     * @param clazz T
     * @return a {@link List} of entities matching the given {@link ListQuery}.
     */
    @Override
    public List<T> findScrollAll(ListQuery query, Class<T> clazz) {
        try {
            if (Objects.isNull(query) || Objects.isNull(query.getQuery())) {
                return Lists.newArrayList(repository.findAll());
            }
            // scroll 存活时间(ms)
            return ElasticSearchUtil.searchScrollAll(restHighLevelClient, query, 60, clazz);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns a {@link PageResult} of entities matching the given {@link PageQuery}.In case no match could be found, an empty
     * {@link PageResult} is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link PageResult} of entities matching the given {@link PageQuery}.
     */
    @Override
    public PageResult<T> findPage(PageQuery query, Class<T> clazz) {
        try {
            NativeSearchQuery searchQuery = new NativeSearchQuery(ElasticSearchUtil.convert(query.getQuery()));
            // 排序
            if (!Objects.isNull(query.getSort()) && !CollectionUtils.isEmpty(query.getSort().getOrder())) {
                query.getSort().getOrder().forEach(it -> {
                    try {
                        String name = it.getName();
                        if (Objects.equals(clazz.getDeclaredField(name).getType(), String.class)) {
                            name = it.getName() + ".keyword";
                        }
                        searchQuery.addSort(Sort.by(Objects.equals(1, it.getAsc()) ? Sort.Order.asc(name)
                                : Sort.Order.desc(name)));
                    } catch (Exception e) {
                        log.error("排序处理异常: ", e);
                    }
                });
            }
            // 分页(ES默认10条)
            Long pageIndex = query.getPageIndex();
            Long pageSize = query.getPageSize();
            searchQuery.setPageable(PageRequest.of(pageIndex.intValue() - 1, pageSize.intValue()));
            SearchHits<T> searchHits;
            Document doc = clazz.getAnnotation(Document.class);
            log.info(">>> searchQuery <<< {}", searchQuery.getQuery());
            if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
                searchHits = elasticsearchOperations.search(searchQuery, clazz, IndexCoordinates.of(doc.indexName()));
            } else {
                searchHits = elasticsearchOperations.search(searchQuery, clazz);
            }
            long total = searchHits.getTotalHits();
            long pages = (long) Math.ceil((float) total / pageSize);
            return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize)
                    .setPages(Objects.equals(0L, pages) ? 1 : pages)
                    .setTotal(total).setRecords(searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns a {@link PageResult} of entities matching the given {@link PageQuery}.In case no match could be found, an empty
     * {@link PageResult} is returned.
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a {@link PageResult} of entities matching the given {@link PageQuery}.
     */
    @Override
    public PageResult<T> findScrollPage(PageQuery query, Class<T> clazz) {
        try {
            return ElasticSearchUtil.searchScrollPage(restHighLevelClient, query, 60 * 1000, clazz);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * Returns entities number matching the given {@link Query}.In case no match could be found, an zero
     *
     * @param query must not be {@literal null}.
     * @param clazz T
     * @return a number
     */
    @Override
    public long count(Query query, Class<T> clazz) {
        try {
            Document doc = clazz.getAnnotation(Document.class);
            if (Objects.isNull(doc) || StringUtils.isBlank(doc.indexName())) {
                return 0;
            }
            return elasticsearchOperations.count(new NativeSearchQuery(ElasticSearchUtil.convert(query)),
                    IndexCoordinates.of(doc.indexName()));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }
}

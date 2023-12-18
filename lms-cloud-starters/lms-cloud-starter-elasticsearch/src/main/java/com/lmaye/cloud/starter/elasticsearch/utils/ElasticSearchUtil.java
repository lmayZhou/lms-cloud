package com.lmaye.cloud.starter.elasticsearch.utils;

import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import com.lmaye.cloud.starter.web.query.Sort;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * -- Elasticsearch Utils
 *
 * @author lmay.Zhou
 * @date 2020/12/1 14:11
 * @email lmay@lmaye.com
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElasticSearchUtil {
    /**
     * 转化请求查询对象为QueryBuilder
     *
     * @param query QueryBuilder
     * @return QueryBuilder
     */
    public static QueryBuilder convert(Query query) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!Objects.isNull(query) && !query.isNull()) {
            boolQueryBuilder.must(convertQuery(query));
        }
        Query must = query.getMust();
        // 且操作
        while (!Objects.isNull(must)) {
            if (!must.isNull()) {
                boolQueryBuilder.must(convertQuery(must));
            }
            must = must.getMust();
        }
        Query should = query.getShould();
        // 或操作
        while (!Objects.isNull(should)) {
            if (!should.isNull()) {
                boolQueryBuilder.should(convertQuery(should));
            }
            should = should.getShould();
        }
        return boolQueryBuilder;
    }

    /**
     * 转换查询
     *
     * @param query QueryBuilder
     * @return QueryBuilder
     */
    private static QueryBuilder convertQuery(Query query) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 等于查询
        if (!CollectionUtils.isEmpty(query.getTerms())) {
            query.getTerms().forEach(q -> {
                if (q.isNegation()) {
                    // fileName加上.keyword方式: 解决QueryBuilders.termQuery(name, value)查询无结果
                    boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(q.getField(), q.getValue()));
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(q.getField(), q.getValue()));
                }
            });
        }
        // 模糊查询
        if (!CollectionUtils.isEmpty(query.getMatches())) {
            query.getMatches().forEach(q -> {
                if (q.isNegation()) {
                    boolQueryBuilder.mustNot(QueryBuilders.matchPhraseQuery(q.getField(), q.getValue()));
                } else {
                    boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(q.getField(), q.getValue()));
                }
            });
        }
        // 范围查询
        if (!CollectionUtils.isEmpty(query.getRanges())) {
            query.getRanges().forEach(q -> {
                if (q.isNegation()) {
                    boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(q.getField()).gte(q.getGe()).lte(q.getLe()));
                } else {
                    boolQueryBuilder.must(QueryBuilders.rangeQuery(q.getField()).gte(q.getGe()).lte(q.getLe()));
                }
            });
        }
        // in查询
        if (!CollectionUtils.isEmpty(query.getIns())) {
            query.getIns().forEach(q -> {
                if (q.isNegation()) {
                    boolQueryBuilder.mustNot(QueryBuilders.termsQuery(q.getField(), q.getValues()));
                } else {
                    boolQueryBuilder.must(QueryBuilders.termsQuery(q.getField(), q.getValues()));
                }
            });
        }
        return boolQueryBuilder;
    }

    /**
     * 获取结果集，返回SearchHit集合
     * - 使用游标
     *
     * @param client          RestHighLevelClient
     * @param query           ListQuery
     * @param scrollAliveTime scroll存活时间(ms)
     * @param clazz           Class<T>
     * @return List<T>
     * @throws Exception 异常
     */
    public static <T> List<T> searchScrollAll(RestHighLevelClient client, ListQuery query,
                                              long scrollAliveTime, Class<T> clazz) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        TimeValue keepAlive = TimeValue.timeValueMillis(scrollAliveTime);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 排序
        Sort sort = query.getSort();
        if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
            sort.getOrder().forEach(it -> {
                searchSourceBuilder.sort(new FieldSortBuilder(it.getName())
                        .order(Objects.equals(1, it.getAsc()) ? SortOrder.ASC : SortOrder.DESC));
            });
        }
        // 浅查5000条
        searchSourceBuilder.size(5000);
        searchSourceBuilder.query(ElasticSearchUtil.convert(query.getQuery()));
        Document doc = clazz.getAnnotation(Document.class);
        if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
            // 索引名称
            searchRequest.indices(doc.indexName());
        }
        searchRequest.source(searchSourceBuilder);
        searchRequest.scroll(keepAlive);
        SearchResponse searchRs = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchRs.getHits().getHits();
        if (ArrayUtils.isEmpty(hits)) {
            return new ArrayList<>();
        }
        List<SearchHit> resultHits = new ArrayList<>();
        String scrollId = searchRs.getScrollId();
        while (ArrayUtils.isNotEmpty(hits)) {
            resultHits.addAll(Arrays.asList(hits));
            SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
            searchScrollRequest.scroll(keepAlive);
            SearchResponse scrollRs = client.scroll(searchScrollRequest, RequestOptions.DEFAULT);
            scrollId = scrollRs.getScrollId();
            hits = scrollRs.getHits().getHits();
        }
        // 清除es快照，释放资源
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        return resultHits.stream().map(it -> GsonUtils.fromJson(it.getSourceAsString(), clazz))
                .collect(Collectors.toList());
    }

    /**
     * 分页查询
     * - 浅分页
     *
     * @param client RestHighLevelClient
     * @param query  PageQuery
     * @param clazz  Class<T>
     * @param <T>    T
     * @return PageResult<T>
     * @throws Exception 异常
     */
    public static <T> PageResult<T> searchPage(RestHighLevelClient client, PageQuery query, Class<T> clazz) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 排序
        Sort sort = query.getSort();
        if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
            sort.getOrder().forEach(it -> {
                searchSourceBuilder.sort(new FieldSortBuilder(it.getName())
                        .order(Objects.equals(1, it.getAsc()) ? SortOrder.ASC : SortOrder.DESC));
            });
        }
        searchSourceBuilder.query(ElasticSearchUtil.convert(query.getQuery()));
        // 分页
        Integer pageIndex = query.getPageIndex() - 1;
        Integer pageSize = query.getPageSize();
        searchSourceBuilder.from(pageIndex * pageSize);
        searchSourceBuilder.size(pageSize);
        Document doc = clazz.getAnnotation(Document.class);
        if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
            // 索引名称
            searchRequest.indices(doc.indexName());
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchRs = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchRs.getHits();
        SearchHit[] hits = searchHits.getHits();
        int total = (int) searchHits.getTotalHits().value;
        int pages = (int) Math.ceil((float) total / pageSize);
        return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize)
                .setPages(Objects.equals(0L, pages) ? 1 : pages)
                .setTotal(total).setRecords(Arrays.stream(hits).map(it ->
                        GsonUtils.fromJson(it.getSourceAsString(), clazz)).collect(Collectors.toList()));
    }

    /**
     * 分页查询
     * - 深度查询(游标)
     *
     * @param client          RestHighLevelClient
     * @param query           PageQuery
     * @param scrollAliveTime scroll存活时间(ms)
     * @param clazz           Class<T>
     * @param <T>             T
     * @return PageResult<T>
     * @throws Exception 异常
     */
    public static <T> PageResult<T> searchScrollPage(RestHighLevelClient client, PageQuery query,
                                                     long scrollAliveTime, Class<T> clazz) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        TimeValue keepAlive = TimeValue.timeValueMillis(scrollAliveTime);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 排序
        Sort sort = query.getSort();
        if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
            sort.getOrder().forEach(it -> {
                searchSourceBuilder.sort(new FieldSortBuilder(it.getName())
                        .order(Objects.equals(1, it.getAsc()) ? SortOrder.ASC : SortOrder.DESC));
            });
        }
        searchSourceBuilder.query(ElasticSearchUtil.convert(query.getQuery()));
        Integer pageIndex = query.getPageIndex();
        Integer pageSize = query.getPageSize();
        // 深度查询ScrollId
        String scrollId = query.getScrollId();
        if (StringUtils.isBlank(scrollId)) {
            // 分页信息(第一次查询分页处理)
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(pageSize);
            Document doc = clazz.getAnnotation(Document.class);
            if (!Objects.isNull(doc) && StringUtils.isNotBlank(doc.indexName())) {
                // 索引名称
                searchRequest.indices(doc.indexName());
            }
            searchRequest.source(searchSourceBuilder);
            searchRequest.scroll(keepAlive);
            SearchResponse searchRs = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchRs.getHits();
            SearchHit[] hits = searchHits.getHits();
            if (ArrayUtils.isEmpty(hits)) {
                return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize).setPages(1).setTotal(0)
                        .setRecords(new ArrayList<>());
            }
            int total = (int) searchHits.getTotalHits().value;
            int pages = (int) Math.ceil((float) total / pageSize);
            return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize)
                    .setPages(Objects.equals(0, pages) ? 1 : pages)
                    .setTotal(total).setRecords(Arrays.stream(hits).map(it -> {
                        Map<String, Object> map = it.getSourceAsMap();
                        map.put("scrollId", searchRs.getScrollId());
                        return GsonUtils.fromJson(GsonUtils.toJson(map), clazz);
                    }).collect(Collectors.toList()));
        }
        // Scroll 模式查询
        SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId);
        searchScrollRequest.scroll(keepAlive);
        SearchResponse searchRs = client.scroll(searchScrollRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchRs.getHits();
        SearchHit[] hits = searchHits.getHits();
        if (ArrayUtils.isEmpty(hits)) {
            return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize).setPages(1).setTotal(0)
                    .setRecords(new ArrayList<>());
        }
        int total = (int) searchHits.getTotalHits().value;
        int pages = (int) Math.ceil((float) total / pageSize);
        return new PageResult<T>().setPageIndex(pageIndex).setPageSize(pageSize)
                .setPages(Objects.equals(0L, pages) ? 1 : pages)
                .setTotal(total).setRecords(Arrays.stream(hits).map(it -> {
                    Map<String, Object> map = it.getSourceAsMap();
                    map.put("scrollId", searchRs.getScrollId());
                    return GsonUtils.fromJson(GsonUtils.toJson(map), clazz);
                }).collect(Collectors.toList()));
    }
}

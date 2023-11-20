package com.lmaye.cloud.starter.mybatis.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lmaye.cloud.core.utils.CoreUtils;
import com.lmaye.cloud.starter.web.query.*;
import com.lmaye.cloud.starter.web.validator.SafeValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * -- MyBatis 工具类
 *
 * @author lmay.Zhou
 * @date 2020/1/2 17:53 星期四
 * @email lmay@lmaye.com
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyBatisUtils {
    /**
     * 转换请求查询对象为QueryWrapper
     *
     * @param query 查询对象
     * @param <T>   泛型
     * @return QueryWrapper<T>
     */
    public static <T> QueryWrapper<T> convert(Query query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (!Objects.isNull(query) && !query.isNull()) {
            convertQuery(queryWrapper, query);
        }
        // 且操作
        Query and = query.getMust();
        while (!Objects.isNull(and)) {
            final Query wrapperQuery = and;
            if (!wrapperQuery.isNull()) {
                queryWrapper.and(it -> convertQuery(it, wrapperQuery));
            }
            and = and.getMust();
        }
        // 或操作
        Query or = query.getShould();
        while (!Objects.isNull(or)) {
            final Query wrapperQuery = or;
            if (!wrapperQuery.isNull()) {
                queryWrapper.or(it -> convertQuery(it, wrapperQuery));
            }
            or = or.getShould();
        }
        return queryWrapper;
    }

    /**
     * 转换查询
     *
     * @param queryWrapper 查询封装
     * @param query        查询对象
     * @param <T>          泛型
     * @return QueryWrapper<T>
     */
    private static <T> QueryWrapper<T> convertQuery(QueryWrapper<T> queryWrapper, Query query) {
        // 等于查询
        convertTermQuery(queryWrapper, query.getTerms());
        // 模糊查询
        convertMatchQuery(queryWrapper, query.getMatches());
        // 范围查询
        convertRangeQuery(queryWrapper, query.getRanges());
        // IN查询
        convertInQuery(queryWrapper, query.getIns());
        return queryWrapper;
    }

    /**
     * 等于查询封装
     *
     * @param queryWrapper 查询封装
     * @param termQueries  等于查询
     * @param <T>          泛型
     */
    private static <T> void convertTermQuery(QueryWrapper<T> queryWrapper, List<TermQuery> termQueries) {
        if (!CollectionUtils.isEmpty(termQueries)) {
            termQueries.forEach(it -> {
                Object value = it.getValue();
                if (!Objects.isNull(value)) {
                    if (value instanceof String) {
                        value = SafeValidator.getSafeStr(value.toString());
                    }
                    final String field = CoreUtils.humpToUnderline(it.getField());
                    if (it.isNegation()) {
                        queryWrapper.lt(field, value);
                        queryWrapper.gt(field, value);
                    } else {
                        queryWrapper.eq(field, value);
                    }
                }
            });
        }
    }

    /**
     * 模糊查询
     *
     * @param queryWrapper 查询封装
     * @param matchQueries 模糊查询
     * @param <T>          泛型
     */
    private static <T> void convertMatchQuery(QueryWrapper<T> queryWrapper, List<MatchQuery> matchQueries) {
        if (!CollectionUtils.isEmpty(matchQueries)) {
            matchQueries.forEach(it -> {
                Object value = it.getValue();
                if (!Objects.isNull(value)) {
                    if (value instanceof String) {
                        value = SafeValidator.getSafeStr(value.toString());
                    }
                    final String field = CoreUtils.humpToUnderline(it.getField());
                    if (it.isNegation()) {
                        queryWrapper.notLike(field, value);
                    } else {
                        queryWrapper.like(field, value);
                    }
                }
            });
        }
    }

    /**
     * 范围查询
     *
     * @param queryWrapper 查询封装
     * @param rangeQueries 范围查询
     * @param <T>          泛型
     */
    private static <T> void convertRangeQuery(QueryWrapper<T> queryWrapper, List<RangeQuery> rangeQueries) {
        if (!CollectionUtils.isEmpty(rangeQueries)) {
            rangeQueries.forEach(it -> {
                Object le = it.getLe();
                if (!Objects.isNull(le)) {
                    if (le instanceof String) {
                        le = SafeValidator.getSafeStr(le.toString());
                    }
                    final String field = CoreUtils.humpToUnderline(it.getField());
                    if (it.isNegation()) {
                        queryWrapper.gt(field, le);
                    } else {
                        queryWrapper.le(field, le);
                    }
                }

                Object ge = it.getGe();
                if (!Objects.isNull(ge)) {
                    if (ge instanceof String) {
                        ge = SafeValidator.getSafeStr(ge.toString());
                    }
                    final String field = CoreUtils.humpToUnderline(it.getField());
                    if (it.isNegation()) {
                        queryWrapper.lt(field, ge);
                    } else {
                        queryWrapper.ge(field, ge);
                    }
                }
            });
        }
    }

    /**
     * IN查询封装
     *
     * @param queryWrapper 查询封装
     * @param inQueries    IN查询
     * @param <T>          泛型
     */
    private static <T> void convertInQuery(QueryWrapper<T> queryWrapper, List<InQuery> inQueries) {
        if (!CollectionUtils.isEmpty(inQueries)) {
            inQueries.forEach(it -> {
                List<Object> values = it.getValues();
                if (!CollectionUtils.isEmpty(values)) {
                    List<Object> valueList = new ArrayList<>();
                    values.forEach(it2 -> {
                        if (!Objects.isNull(it2)) {
                            if (it2 instanceof String) {
                                it2 = SafeValidator.getSafeStr(it2.toString());
                            }
                            valueList.add(it2);
                        }
                    });
                    final String field = CoreUtils.humpToUnderline(it.getField());
                    if (it.isNegation()) {
                        queryWrapper.notIn(field, valueList);
                    } else {
                        queryWrapper.in(field, valueList);
                    }
                }
            });
        }
    }
}

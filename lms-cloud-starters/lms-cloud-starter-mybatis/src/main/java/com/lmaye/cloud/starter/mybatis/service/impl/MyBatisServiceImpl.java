package com.lmaye.cloud.starter.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmaye.cloud.core.constants.YesOrNo;
import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.mybatis.repository.IMyBatisRepository;
import com.lmaye.cloud.starter.mybatis.service.IMyBatisService;
import com.lmaye.cloud.starter.mybatis.utils.MyBatisUtils;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import com.lmaye.cloud.starter.web.query.Sort;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * -- MyBatis Service 实现类
 *
 * @author lmay.Zhou
 * @date 2020/1/2 16:50 星期四
 * @email lmay@lmaye.com
 */
public class MyBatisServiceImpl<M extends IMyBatisRepository<T>, T, ID extends Serializable> extends ServiceImpl<M, T>
        implements IMyBatisService<T, ID> {
    private static final int BATCH_SIZE = 1000;

    @Override
    public M getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <S extends T> S insertOrUpdate(S entity) throws ServiceException {
        try {
            if (!Objects.isNull(entity)) {
                Class<?> clazz = entity.getClass();
                TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
                Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
                String keyProperty = tableInfo.getKeyProperty();
                Assert.notNull(keyProperty, "error: can not execute. because can not find column for id from entity!");
                Object idVal = ReflectionKit.getFieldValue(entity, keyProperty);
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    save(entity);
                } else {
                    updateById(entity);
                }
            }
            return entity;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) throws ServiceException {
        try {
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            try (SqlSession session = sqlSessionBatch()) {
                int i = 0;
                for (T entity : entities) {
                    session.insert(sqlStatement, entity);
                    if (i >= 1 && i % BATCH_SIZE == 0) {
                        session.flushStatements();
                    }
                    i++;
                }
                session.flushStatements();
            }
            return entities;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public void deleteById(ID id) throws ServiceException {
        try {
            removeById(id);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public Optional<T> findById(ID id) throws ServiceException {
        try {
            return Optional.ofNullable(getById(id));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public List<T> findAll(Query query) throws ServiceException {
        try {
            if (Objects.isNull(query)) {
                return super.list();
            }
            return super.list(MyBatisUtils.convert(query));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public List<T> findAll(ListQuery query) throws ServiceException {
        try {
            if (Objects.isNull(query) || Objects.isNull(query.getQuery())) {
                if (!Objects.isNull(query)) {
                    Sort sort = query.getSort();
                    if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
                        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
                        sort.getOrder().forEach(o -> queryWrapper.orderBy(true,
                                Objects.equals(YesOrNo.YES.getCode(), o.getAsc()), o.getName()));
                        return super.list(queryWrapper);
                    }
                }
                return super.list();
            }
            QueryWrapper<T> queryWrapper = getQueryWrapper(query.getQuery(), query.getSort());
            return super.list(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public PageResult<T> findAll(PageQuery query) throws ServiceException {
        try {
            IPage<T> page;

            if (Objects.isNull(query.getQuery())) {
                Sort sort = query.getSort();
                if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
                    QueryWrapper<T> queryWrapper = new QueryWrapper<>();
                    sort.getOrder().forEach(o -> queryWrapper.orderBy(true,
                            Objects.equals(YesOrNo.YES.getCode(), o.getAsc()), o.getName()));
                    page = super.page(new Page<>(query.getPageIndex(), query.getPageSize()), queryWrapper);
                } else {
                    page = super.page(new Page<>(query.getPageIndex(), query.getPageSize()));
                }
            } else {
                QueryWrapper<T> queryWrapper = getQueryWrapper(query.getQuery(), query.getSort());
                page = super.page(new Page<>(query.getPageIndex(), query.getPageSize()), queryWrapper);
            }
            return new PageResult<T>().setPageIndex(page.getCurrent()).setPageSize(page.getSize())
                    .setPages(page.getPages()).setTotal(page.getTotal()).setRecords(page.getRecords());
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public long count(Query query) throws ServiceException {
        try {
            if (Objects.isNull(query)) {
                return super.count();
            }
            return super.count(MyBatisUtils.convert(query));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 获取查询Wrapper
     *
     * @param query TortoiseQuery
     * @param sort  TortoiseSort
     * @return QueryWrapper<T>
     */
    protected QueryWrapper<T> getQueryWrapper(Query query, Sort sort) {
        QueryWrapper<T> queryWrapper = MyBatisUtils.convert(query);
        // 排序
        if (!Objects.isNull(sort) && !CollectionUtils.isEmpty(sort.getOrder())) {
            sort.getOrder().forEach(o -> queryWrapper.orderBy(true,
                    Objects.equals(YesOrNo.YES.getCode(), o.getAsc()), o.getName()));
        }
        return queryWrapper;
    }
}

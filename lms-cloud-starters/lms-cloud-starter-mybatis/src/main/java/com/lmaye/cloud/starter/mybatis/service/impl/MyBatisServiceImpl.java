package com.lmaye.cloud.starter.mybatis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * -- MyBatis Service 实现类
 *
 * @author lmay.Zhou
 * @date 2020/1/2 16:50 星期四
 * @email lmay@lmaye.com
 */
public class MyBatisServiceImpl<M extends IMyBatisRepository<T>, T, ID extends Serializable> extends ServiceImpl<M, T>
        implements IMyBatisService<T, ID> {

    @Override
    public M getBaseMapper() {
        return super.getBaseMapper();
    }

    @Override
    public <S extends T> S insertOrUpdate(S entity) throws ServiceException {
        try {
            if (saveOrUpdate(entity)) {
                return entity;
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> saveAll(List<T> entities) throws ServiceException {
        try {
            if (saveBatch(entities)) {
                return entities;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public boolean deleteById(ID id) throws ServiceException {
        try {
            return removeById(id);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public boolean deleteByIds(List<ID> ids) throws ServiceException {
        try {
            return removeByIds(ids);
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public T findById(ID id) throws ServiceException {
        try {
            return getById(id);
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
            return super.list(getQueryWrapper(query.getQuery(), query.getSort()));
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    @Override
    public PageResult<T> findPage(PageQuery query) throws ServiceException {
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

package com.example.swagger.service;

import com.example.swagger.entity.Test;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.web.context.PageResult;
import com.lmaye.cloud.starter.web.query.ListQuery;
import com.lmaye.cloud.starter.web.query.PageQuery;
import com.lmaye.cloud.starter.web.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * --
 *
 * @author Lmay Zhou
 * @date 2023/11/7 23:51
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public <S extends Test> S insertOrUpdate(S entity) throws ServiceException {
        return null;
    }

    @Override
    public List<Test> saveAll(List<Test> entities) throws ServiceException {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) throws ServiceException {
        return false;
    }

    @Override
    public boolean deleteByIds(List<Long> longs) throws ServiceException {
        return false;
    }

    @Override
    public Test findById(Long aLong) throws ServiceException {
        return null;
    }

    @Override
    public List<Test> findAll(Query query) throws ServiceException {
        return null;
    }

    @Override
    public List<Test> findAll(ListQuery query) throws ServiceException {
        return null;
    }

    @Override
    public PageResult<Test> findPage(PageQuery query) throws ServiceException {
        return null;
    }

    @Override
    public long count(Query query) throws ServiceException {
        return 0;
    }
}

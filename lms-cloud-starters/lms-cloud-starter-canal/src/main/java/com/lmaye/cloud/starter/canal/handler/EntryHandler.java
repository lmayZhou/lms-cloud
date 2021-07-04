package com.lmaye.cloud.starter.canal.handler;

/**
 * -- EntryHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public interface EntryHandler<T> {
    /**
     * DDL操作
     *
     * @param sql DDL SQL语句
     */
    void ddlEvent(String sql);

    /**
     * 新增
     *
     * @param t Object
     */
    void insert(T t);

    /**
     * 修改
     *
     * @param before Object
     * @param after  Object
     */
    void update(T before, T after);

    /**
     * 删除
     *
     * @param t Object
     */
    void delete(T t);
}

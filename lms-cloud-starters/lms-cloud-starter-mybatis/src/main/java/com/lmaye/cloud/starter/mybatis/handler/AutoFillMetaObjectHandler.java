package com.lmaye.cloud.starter.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lmaye.cloud.core.constants.CoreConstants;
import com.lmaye.cloud.core.constants.YesOrNo;
import com.lmaye.cloud.starter.web.context.UserToken;
import com.lmaye.cloud.starter.web.utils.HttpUtils;
import com.lmaye.cloud.starter.web.utils.TokenUtils;
import org.apache.ibatis.reflection.MetaObject;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * -- 自动填充公共字段
 *
 * @author lmay.Zhou
 * @date 2019/12/6 17:41 星期五
 * @email lmay@lmaye.com
 */
public class AutoFillMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        final HttpServletRequest request = HttpUtils.getRequest();
        if (Objects.nonNull(request)) {
            final UserToken userToken = TokenUtils.parsUserInfo(request.getHeader(CoreConstants.FIELD_AUTHORIZATION));
            if (Objects.nonNull(userToken)) {
                final Long userId = userToken.getId();
                setFieldValByName("createdBy", userId, metaObject);
                setFieldValByName("lastModifiedBy", userId, metaObject);
            }
        }
        setFieldValByName("createdAt", now, metaObject);
        setFieldValByName("deleted", YesOrNo.NO.getCode(), metaObject);
        setFieldValByName("lastModifiedAt", now, metaObject);
        setFieldValByName("version", CoreConstants.VERSION, metaObject);
    }

    /**
     * 更新填充
     *
     * @param metaObject metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        final HttpServletRequest request = HttpUtils.getRequest();
        if (Objects.nonNull(request)) {
            final UserToken userToken = TokenUtils.parsUserInfo(request.getHeader(CoreConstants.FIELD_AUTHORIZATION));
            if (Objects.nonNull(userToken)) {
                setFieldValByName("lastModifiedBy", userToken.getId(), metaObject);
            }
        }
        setFieldValByName("lastModifiedAt", LocalDateTime.now(), metaObject);
    }
}

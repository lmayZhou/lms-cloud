package com.lmaye.cloud.starter.mybatis.handler;

import com.lmaye.cloud.starter.mybatis.type.JsonType;
import com.lmaye.cloud.starter.web.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * -- Json Type Handler
 *
 * @author lmay.Zhou
 * @date 2019/12/6 18:29 星期五
 * @email lmay@lmaye.com
 */
public class JsonTypeHandler extends BaseTypeHandler<JsonType> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonType parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, JsonUtils.toStr(parameter));
    }

    @Override
    public JsonType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String val = rs.getString(columnName);
        if(StringUtils.isBlank(val)) {
            return null;
        }
        return new JsonType().setVal(val);
    }

    @Override
    public JsonType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String val = rs.getString(columnIndex);
        if(StringUtils.isBlank(val)) {
            return null;
        }
        return new JsonType().setVal(val);
    }

    @Override
    public JsonType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String val = cs.getString(columnIndex);
        if(StringUtils.isBlank(val)) {
            return null;
        }
        return new JsonType().setVal(val);
    }
}

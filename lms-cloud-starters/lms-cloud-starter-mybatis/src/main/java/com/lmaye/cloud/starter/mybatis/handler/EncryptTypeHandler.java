package com.lmaye.cloud.starter.mybatis.handler;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.lmaye.cloud.starter.mybatis.type.EncryptType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * -- 数据加密
 *
 * @author lmay.Zhou
 * @date 2020/1/2 11:42 星期四
 * @email lmay@lmaye.com
 */
public class EncryptTypeHandler extends BaseTypeHandler<EncryptType> {
    private static final String KEY = "bG1heQ==";
    private static final String IV = "WmhvdQ==";
    public static final AES AES = new AES(Mode.CBC, Padding.PKCS5Padding, KEY.getBytes(), IV.getBytes());

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EncryptType parameter, JdbcType jdbcType) throws SQLException {
        if(parameter.isNull()) {
            ps.setString(i, "");
        } else {
            String encryptVal = AES.encryptBase64(parameter.getVal());
            ps.setString(i, encryptVal);
        }
    }

    @Override
    public EncryptType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        return getColumnValue(value);
    }

    @Override
    public EncryptType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        return getColumnValue(value);
    }

    @Override
    public EncryptType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        return getColumnValue(value);
    }

    /**
     * 获取解密后的值，解密失败返回原值
     *
     * @param value 值
     * @return EncryptType
     */
    private EncryptType getColumnValue(String value) {
        EncryptType encryptType = new EncryptType();
        encryptType.setEncryptVal(value);
        try {
            encryptType.setVal(AES.decryptStr(value));
        } catch (Exception e) {
            encryptType.setVal(value);
        }
        return encryptType;
    }
}

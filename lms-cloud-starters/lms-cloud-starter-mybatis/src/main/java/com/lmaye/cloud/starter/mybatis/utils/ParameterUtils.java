package com.lmaye.cloud.starter.mybatis.utils;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.symmetric.AES;
import com.lmaye.cloud.starter.mybatis.MybatisExtProperties;
import com.lmaye.cloud.starter.mybatis.annotation.FieldSensitive;
import com.lmaye.cloud.starter.web.utils.SpringUtils;

import java.util.Objects;

/**
 * -- ParameterUtils
 *
 * @author Lmay Zhou
 * @date 2023/11/17 17:19
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public final class ParameterUtils {
    /**
     * MybatisPlusExtProperties
     */
    private final static MybatisExtProperties MYBATIS_PLUS_EXT_PROPERTIES;

    static {
        MYBATIS_PLUS_EXT_PROPERTIES = SpringUtils.getBean("mybatisExtProperties", MybatisExtProperties.class);
    }

    /**
     * 数据脱敏
     *
     * @param fieldSensitive 字段脱敏类型
     * @param source         源数据
     * @return String
     */
    public static String fieldSensitiveData(FieldSensitive fieldSensitive, String source) {
        final String data;
        switch (fieldSensitive.value()) {
            case ALL:
                data = DesensitizedUtil.password(source);
                break;
            case NAME:
                data = DesensitizedUtil.chineseName(source);
                break;
            case EMAIL:
                data = DesensitizedUtil.email(source);
                break;
            case MOBILE:
                data = DesensitizedUtil.mobilePhone(source);
                break;
            case ID_CARD:
                data = DesensitizedUtil.idCardNum(source, 1, 2);
                break;
            case PASSWORD:
                data = DesensitizedUtil.clear();
                break;
            case BANK_CARD:
                data = DesensitizedUtil.bankCard(source);
                break;
            default:
                data = DesensitizedUtil.firstMask(source);
        }
        return data;
    }

    /**
     * 数据加密
     *
     * @param source 源数据
     * @return String
     */
    public static String fieldEncrypt(String source) {
        final MybatisExtProperties.Aes aes = MYBATIS_PLUS_EXT_PROPERTIES.getAes();
        if (Objects.nonNull(aes)) {
            return new AES(aes.getMode(), aes.getPadding(), aes.getKey().getBytes(), aes.getIv().getBytes())
                    .encryptBase64(source);
        }
        return source;
    }

    /**
     * 数据解密
     *
     * @param source 源数据
     * @return String
     */
    public static String fieldDecrypt(String source) {
        final MybatisExtProperties.Aes aes = MYBATIS_PLUS_EXT_PROPERTIES.getAes();
        if (Objects.nonNull(aes)) {
            return new AES(aes.getMode(), aes.getPadding(), aes.getKey().getBytes(), aes.getIv().getBytes())
                    .decryptStr(source);
        }
        return source;
    }
}

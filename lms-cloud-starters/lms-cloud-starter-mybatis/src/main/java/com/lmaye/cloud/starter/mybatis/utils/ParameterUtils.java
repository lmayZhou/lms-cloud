package com.lmaye.cloud.starter.mybatis.utils;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SM4;
import com.lmaye.cloud.starter.mybatis.MybatisExtProperties;
import com.lmaye.cloud.starter.mybatis.constant.EnDecryptType;
import com.lmaye.cloud.starter.mybatis.constant.SensitiveType;
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
        // init
        MYBATIS_PLUS_EXT_PROPERTIES = SpringUtils.getBean("mybatisExtProperties", MybatisExtProperties.class);
    }

    /**
     * 数据脱敏
     *
     * @param type   字段脱敏类型
     * @param source 源数据
     * @return String
     */
    public static String fieldSensitiveData(SensitiveType type, String source) {
        final String data;
        switch (type) {
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
     * @param type   加解密算法
     * @param source 源数据
     * @return String
     */
    public static String fieldEncrypt(EnDecryptType type, String source) {
        final MybatisExtProperties.Symmetric symmetric = MYBATIS_PLUS_EXT_PROPERTIES.getSymmetric();
        final MybatisExtProperties.Asymmetric asymmetric = MYBATIS_PLUS_EXT_PROPERTIES.getAsymmetric();
        if (Objects.isNull(symmetric) && Objects.isNull(asymmetric)) {
            return source;
        }
        final String data;
        switch (type) {
            case AES:
                data = new AES(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).encryptBase64(source);
                break;
            case DES:
                data = new DES(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).encryptBase64(source);
                break;
            case SM4:
                data = new SM4(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).encryptBase64(source);
                break;
            case SM2:
                data = new SM2(asymmetric.getPrivateKey(), asymmetric.getPublicKey()).encryptBase64(source, KeyType.PublicKey);
                break;
            case RSA:
                data = new RSA(asymmetric.getPrivateKey(), asymmetric.getPublicKey()).encryptBase64(source, KeyType.PublicKey);
                break;
            default:
                data = source;
        }
        return data;
    }

    /**
     * 数据解密
     *
     * @param type   加解密算法
     * @param source 源数据
     * @return String
     */
    public static String fieldDecrypt(EnDecryptType type, String source) {
        final MybatisExtProperties.Symmetric symmetric = MYBATIS_PLUS_EXT_PROPERTIES.getSymmetric();
        final MybatisExtProperties.Asymmetric asymmetric = MYBATIS_PLUS_EXT_PROPERTIES.getAsymmetric();
        if (Objects.isNull(symmetric) && Objects.isNull(asymmetric)) {
            return source;
        }
        final String data;
        switch (type) {
            case AES:
                data = new AES(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).decryptStr(source);
                break;
            case DES:
                data = new DES(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).decryptStr(source);
                break;
            case SM4:
                data = new SM4(symmetric.getMode(), symmetric.getPadding(), symmetric.getKey().getBytes(),
                        symmetric.getIv().getBytes()).decryptStr(source);
                break;
            case SM2:
                data = new SM2(asymmetric.getPrivateKey(), asymmetric.getPublicKey()).decryptStr(source, KeyType.PrivateKey);
                break;
            case RSA:
                data = new RSA(asymmetric.getPrivateKey(), asymmetric.getPublicKey()).decryptStr(source, KeyType.PrivateKey);
                break;
            default:
                data = source;
        }
        return data;
    }
}

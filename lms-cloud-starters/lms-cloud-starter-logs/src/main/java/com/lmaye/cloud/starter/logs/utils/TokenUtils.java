package com.lmaye.cloud.starter.logs.utils;

import com.lmaye.cloud.core.utils.GsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * -- Token Utils
 *
 * @author Lmay Zhou
 * @date 2021/11/4 17:52
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public class TokenUtils {
    /**
     * 解析用户信息
     *
     * @param token Token
     * @return JSONObject
     */
    public static JSONObject parsingUserInfo(String token) {
        try {
            String userInfo = token.substring(token.indexOf("."), token.lastIndexOf("."));
            return GsonUtils.fromJson(StringUtils.newStringUtf8(Base64.decodeBase64(userInfo.getBytes())), JSONObject.class);
        } catch (Exception e) {
            return null;
        }
    }
}

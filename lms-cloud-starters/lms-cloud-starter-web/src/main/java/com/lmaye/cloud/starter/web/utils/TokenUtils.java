package com.lmaye.cloud.starter.web.utils;

import com.lmaye.cloud.starter.web.context.UserToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

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
    public static UserToken parsUserInfo(String token) {
        try {
            String userInfo = token.substring(token.indexOf("."), token.lastIndexOf("."));
            return JsonUtils.toBean(StringUtils.newStringUtf8(Base64.decodeBase64(userInfo.getBytes())), UserToken.class);
        } catch (Exception e) {
            return null;
        }
    }
}

package com.lmaye.cloud.starter.oauth2.utils;

import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.web.context.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * -- Token Decode
 *
 * @author lmay.Zhou
 * @date 2020/12/23 17:27
 * @email lmay@lmaye.com
 */
@Slf4j
public class TokenDecode {
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "public.key";

    /**
     * 获取令牌
     *
     * @return String
     */
    public String getToken() {
        OAuth2AuthenticationDetails authentication = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext()
                .getAuthentication().getDetails();
        return authentication.getTokenValue();
    }

    /**
     * 获取当前的登录的用户的用户信息
     *
     * @return UserToken
     */
    public UserToken getUserInfo() {
        final String pubKey = getPubKey();
        if (Objects.isNull(pubKey)) {
            return null;
        }
        Jwt jwt = JwtHelper.decodeAndVerify(getToken(), new RsaVerifier(pubKey));
        return GsonUtils.fromJson(jwt.getClaims(), UserToken.class);
    }

    /**
     * 获取公钥
     *
     * @return String
     */
    public String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try (InputStreamReader streamReader = new InputStreamReader(resource.getInputStream());
             BufferedReader br = new BufferedReader(streamReader)) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            log.error("公钥读取异常: ", e);
        }
        return null;
    }
}

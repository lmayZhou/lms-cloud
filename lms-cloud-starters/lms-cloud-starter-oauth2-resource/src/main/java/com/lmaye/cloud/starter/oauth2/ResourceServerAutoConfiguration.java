package com.lmaye.cloud.starter.oauth2;

import com.lmaye.cloud.starter.oauth2.component.AuthExceptionEntryPoint;
import com.lmaye.cloud.starter.oauth2.component.CustomAccessDeniedHandler;
import com.lmaye.cloud.starter.oauth2.utils.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * -- Resource Server Auto Configuration
 *
 * @author lmay.Zhou
 * @date 2021/5/31 09:37
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Configuration
@EnableResourceServer
@AutoConfigureAfter(OAuth2AutoConfiguration.class)
@EnableConfigurationProperties(Oauth2ResourceProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ResourceServerAutoConfiguration extends ResourceServerConfigurerAdapter {
    /**
     * Web Properties
     */
    @Autowired
    private Oauth2ResourceProperties oauth2ResourceProperties;

    /**
     * TokenDecode
     *
     * @return TokenDecode
     */
    @Bean
    TokenDecode tokenDecode() {
        return new TokenDecode();
    }

    /**
     * 定义JwtTokenStore
     *
     * @param jwtAccessTokenConverter JwtAccessTokenConverter
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 定义JwtAccessTokenConverter用来校验令牌
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(tokenDecode().getPubKey());
        return converter;
    }

    /**
     * 配置自定义响应
     *
     * @param resources ResourceServerSecurityConfigurer
     * @throws Exception 异常
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 自定义异常
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * Http安全配置，对每个到达系统的http请求链接进行校验
     *
     * @param http HttpSecurity
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置地址放行
        List<String> permitUris = oauth2ResourceProperties.getIgnoreUris();
        http.authorizeRequests().antMatchers(CollectionUtils.isEmpty(permitUris)
                ? new String[0] : permitUris.toArray(new String[0])).permitAll().anyRequest().authenticated();
    }
}

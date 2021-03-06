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
     * ??????JwtTokenStore
     *
     * @param jwtAccessTokenConverter JwtAccessTokenConverter
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * ??????JwtAccessTokenConverter??????????????????
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
     * ?????????????????????
     *
     * @param resources ResourceServerSecurityConfigurer
     * @throws Exception ??????
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // ???????????????
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * Http???????????????????????????????????????http????????????????????????
     *
     * @param http HttpSecurity
     * @throws Exception ??????
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // ??????????????????
        List<String> permitUris = oauth2ResourceProperties.getIgnoreUris();
        http.authorizeRequests().antMatchers(CollectionUtils.isEmpty(permitUris)
                ? new String[0] : permitUris.toArray(new String[0])).permitAll().anyRequest().authenticated();
    }
}

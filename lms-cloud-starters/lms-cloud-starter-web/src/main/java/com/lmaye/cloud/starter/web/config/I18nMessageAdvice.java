package com.lmaye.cloud.starter.web.config;

import com.lmaye.cloud.starter.web.WebProperties;
import com.lmaye.cloud.starter.web.context.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * -- Response Advice
 *
 * @author Lmay Zhou
 * @date 2021/3/11 16:32
 * @email lmay@lmaye.com
 */
@RestControllerAdvice
@ConditionalOnProperty(value = "i18n.enabled", prefix = "web", matchIfMissing = true)
public class I18nMessageAdvice implements ResponseBodyAdvice<Object> {
    /**
     * Message Source
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * WebProperties
     */
    @Autowired
    private WebProperties webProperties;

    @Override
    public boolean supports(MethodParameter methodParam, Class<? extends HttpMessageConverter<?>> clazz) {
        return true;
    }

    /**
     * 响应前处理
     *
     * @param body        响应body
     * @param methodParam 方法参数
     * @param mediaType   媒体类型
     * @param clazz       HttpMessageConverter
     * @param request     请求
     * @param response    响应
     * @return Object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(Object body, MethodParameter methodParam, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> clazz, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (MediaType.APPLICATION_JSON.equals(mediaType) && body instanceof ResultVO) {
            ResultVO<Object> rs = (ResultVO<Object>) body;
            try {
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                WebProperties.I18n i18n = webProperties.getI18n();
                String language = servletRequest.getParameter(i18n.getLocaleName());
                Locale locale;
                if (StringUtils.isNotBlank(language)) {
                    // 自定义请求路径参数( ?locale=en-US )
                    String[] split = language.split(i18n.getLocaleDelimiter());
                    locale = split.length == 2 ? new Locale(split[0], split[1]) : Locale.SIMPLIFIED_CHINESE;
                } else {
                    // 自定义请求头参数( locale=en-US )
                    language = servletRequest.getHeader(i18n.getLocaleName());
                    if (StringUtils.isNotBlank(language)) {
                        String[] split = language.split(i18n.getLocaleDelimiter());
                        locale = split.length == 2 ? new Locale(split[0], split[1]) : Locale.SIMPLIFIED_CHINESE;
                    } else {
                        // 请求头参数( Accept-Language : en-US )
                        locale = LocaleContextHolder.getLocale();
                    }
                }
                rs.setMsg(messageSource.getMessage(rs.getMsg(), null, locale));
            } catch (Exception e) {
                // 已转换 || 未找到key
            }
            return rs;
        }
        return body;
    }
}

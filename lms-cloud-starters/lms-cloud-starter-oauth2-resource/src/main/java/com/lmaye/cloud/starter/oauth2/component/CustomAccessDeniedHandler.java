package com.lmaye.cloud.starter.oauth2.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.starter.web.context.ResultVO;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * -- Custom Access Denied Handler
 *
 * @author lmay.Zhou
 * @date 2020/12/24 15:01
 * @email lmay@lmaye.com
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        // 权限不足处理
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(mapper.writeValueAsString(new ResultVO<>(ResultCode.UNAUTHORIZED,
                accessDeniedException.getMessage())));
    }
}

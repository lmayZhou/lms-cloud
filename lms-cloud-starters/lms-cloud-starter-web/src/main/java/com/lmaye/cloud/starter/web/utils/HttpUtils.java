package com.lmaye.cloud.starter.web.utils;

import cn.hutool.core.convert.Convert;
import com.lmaye.cloud.core.context.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * -- HttpUtils
 *
 * @author lmay.Zhou
 * @date 2021/7/26 14:20
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public final class HttpUtils {
    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取OS
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String userAgentLowerCase = userAgent.toLowerCase();
        if (userAgentLowerCase.contains("windows")) {
            return "Windows";
        } else if (userAgentLowerCase.contains("mac")) {
            return "Mac";
        } else if (userAgentLowerCase.contains("x11")) {
            return "Unix";
        } else if (userAgentLowerCase.contains("android")) {
            return "Android";
        } else if (userAgentLowerCase.contains("iphone")) {
            return "IPhone";
        }
        return "UnKnown, More-Info: " + userAgent;
    }

    /**
     * 获取浏览器信息
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String userAgentLowerCase = userAgent.toLowerCase();
        if (userAgentLowerCase.contains("edge")) {
            return (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (userAgentLowerCase.contains("msie")) {
            String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            return substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else if (userAgentLowerCase.contains("safari") && userAgentLowerCase.contains("version")) {
            return (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (userAgentLowerCase.contains("opr") || userAgentLowerCase.contains("opera")) {
            if (userAgentLowerCase.contains("opera")) {
                return (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (userAgentLowerCase.contains("opr")) {
                return ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }
        } else if (userAgentLowerCase.contains("chrome")) {
            return (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((userAgentLowerCase.contains("mozilla/7.0")) || (userAgentLowerCase.contains("netscape6")) ||
                (userAgentLowerCase.contains("mozilla/4.7")) || (userAgentLowerCase.contains("mozilla/4.78")) ||
                (userAgentLowerCase.contains("mozilla/4.08")) || (userAgentLowerCase.contains("mozilla/3"))) {
            return "Netscape-?";
        } else if (userAgentLowerCase.contains("firefox")) {
            return (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (userAgentLowerCase.contains("rv")) {
            String ieVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            return "IE" + ieVersion.substring(0, ieVersion.length() - 1);
        }
        return "UnKnown, More-Info: " + userAgent;
    }

    /**
     * 获取String参数
     *
     * @return String
     */
    public static String getParameter(String name) {
        final HttpServletRequest request = getRequest();
        return Objects.isNull(request) ? null : request.getParameter(name);
    }

    /**
     * 获取String参数
     *
     * @param name         属性名
     * @param defaultValue 默认值
     * @return String
     */
    public static String getParameter(String name, String defaultValue) {
        final HttpServletRequest request = getRequest();
        return Objects.isNull(request) ? null : Convert.toStr(request.getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     *
     * @param name 属性名
     * @return Integer
     */
    public static Integer getParameterToInt(String name) {
        final HttpServletRequest request = getRequest();
        return Objects.isNull(request) ? null : Convert.toInt(request.getParameter(name));
    }

    /**
     * 获取Integer参数
     *
     * @param name         属性名
     * @param defaultValue 默认值
     * @return Integer
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        final HttpServletRequest request = getRequest();
        return Objects.isNull(request) ? null : Convert.toInt(request.getParameter(name), defaultValue);
    }

    /**
     * 获取request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        final ServletRequestAttributes requestAttributes = getRequestAttributes();
        return Objects.isNull(requestAttributes) ? null : requestAttributes.getRequest();
    }

    /**
     * 获取response
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        final ServletRequestAttributes requestAttributes = getRequestAttributes();
        return Objects.isNull(requestAttributes) ? null : requestAttributes.getResponse();
    }

    /**
     * 获取session
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        final HttpServletRequest request = getRequest();
        return Objects.isNull(request) ? null : request.getSession();
    }

    /**
     * ServletRequestAttributes
     *
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes getRequestAttributes() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return Objects.isNull(requestAttributes) ? null : (ServletRequestAttributes) requestAttributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param data     待渲染的内容
     * @return String
     */
    public static String renderString(HttpServletResponse response, String data) {
        try {
            response.setStatus(ResultCode.SUCCESS.getCode());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

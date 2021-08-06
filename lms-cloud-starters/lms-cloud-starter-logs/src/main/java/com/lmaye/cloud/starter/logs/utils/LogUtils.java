package com.lmaye.cloud.starter.logs.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * -- Log Utils
 *
 * @author lmay.Zhou
 * @date 2021/7/26 14:20
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public final class LogUtils {
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
        } else if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        } else if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
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
}

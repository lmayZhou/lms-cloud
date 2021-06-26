package com.lmaye.cloud.core.utils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * -- 请求路径工具类
 *
 * @author lmay.Zhou
 * @date 2021/5/30 16:35
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public final class RequestUriUtils {
    /**
     * 将通配符表达式转化为正则表达式
     *
     * @param path 路径
     * @return String
     */
    public static String getRegPath(String path) {
        char[] chars = path.toCharArray();
        int len = chars.length;
        StringBuilder sb = new StringBuilder();
        boolean preX = false;
        for (int i = 0; i < len; i++) {
            if (chars[i] == '*') {
                // 遇到*字符
                if (preX) {
                    // 如果是第二次遇到*，则将**替换成.*
                    sb.append(".*");
                    preX = false;
                } else if (i + 1 == len) {
                    // 如果是遇到单星，且单星是最后一个字符，则直接将*转成[^/]*
                    sb.append("[^/]*");
                } else {
                    // 否则单星后面还有字符，则不做任何处理
                    preX = true;
                }
            } else {
                // 遇到非*字符
                if (preX) {
                    // 如果上一次是*，则先把上一次的*对应的[^/]*添进来
                    sb.append("[^/]*");
                    preX = false;
                }
                if (chars[i] == '?') {
                    // 接着判断当前字符是不是?，是的话替换成.
                    sb.append('.');
                } else {
                    // 不是?的话，则就是普通字符，直接添进来
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 通配符模式
     *
     * @param whiteUri 白名单URI
     * @param reqUrl   请求地址
     * @return boolean
     */
    public static boolean filterUrls(String whiteUri, String reqUrl) {
        return Pattern.compile(getRegPath(whiteUri)).matcher(reqUrl).matches();
    }

    /**
     * 校验白名单
     *
     * @param whiteUris 白名单URI列表
     * @param reqUri    请求URI
     * @return boolean
     */
    public static boolean checkWhites(List<String> whiteUris, String reqUri) {
        return whiteUris.stream().anyMatch(url -> filterUrls(url, reqUri));
    }
}

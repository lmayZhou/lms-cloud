package com.lmaye.cloud.core.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * -- 字符串操作工具类
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
public final class StringCoreUtils {
    private static final int INDEX_NOT_FOUND = -1;
    /**
     * 校验包含大写字母的正则表达式
     */
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 生成32位的Uuid
     *
     * @return 字符串
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生产随机字符串
     *
     * @param length 字符串长度
     * @return String
     */
    public static String generateStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 转下划线
     *
     * @param str 字符串
     * @return String
     */
    public static String toUnderline(String str) {
        Assert.notEmpty(str, "The transform string cannot be empty");
        Matcher matcher = UPPERCASE_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 截取字符串，如果超出最大长度，则截取到最大长度-3的位数再加上“...”
     *
     * @param str    字符串
     * @param length 最大长度
     * @return String
     */
    public static String substr(String str, int length) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    /**
     * 截取字符串，被截掉的部分用*代替
     *
     * @param str   字符串
     * @param start 前面显示位数
     * @param end   后面显示位数
     * @return String
     */
    public static String substr(String str, int start, int end) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (str.length() <= start + end) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < (str.length() - end); i++) {
            stringBuilder.append("*");
        }
        return str.substring(0, start) + stringBuilder.toString() + str.substring(str.length() - end);
    }

    /**
     * 编码字符串
     * - 使用系统默认编码
     *
     * @param str 字符串
     * @return byte[]
     */
    public static byte[] bytes(CharSequence str) {
        return bytes(str, Charset.defaultCharset());
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return byte[]
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (Objects.isNull(str)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

    /**
     * 解码字节码
     *
     * @param data 字符串
     * @return String
     */
    public static String str(byte[] data) {
        if (Objects.isNull(data)) {
            return null;
        }
        return new String(data, Charset.defaultCharset());
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return String
     */
    public static String str(byte[] data, Charset charset) {
        if (Objects.isNull(data)) {
            return null;
        }
        if (Objects.isNull(charset)) {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     * 指定范围内查找字符串，忽略大小写<br>
     *
     * <pre>
     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @return int
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    /**
     * 指定范围内查找字符串
     *
     * <pre>
     * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
     * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
     * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
     * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
     * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
     * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
     * </pre>
     *
     * @param str       字符串
     * @param searchStr 需要查找位置的字符串
     * @param fromIndex 起始位置
     * @return int
     * @since 3.2.1
     */
    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    /**
     * 指定范围内反向查找字符串
     *
     * @param str        字符串
     * @param searchStr  需要查找位置的字符串
     * @param fromIndex  起始位置
     * @param ignoreCase 是否忽略大小写
     * @return int
     * @since 3.2.1
     */
    public static int indexOf(final CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (Objects.isNull(str) || Objects.isNull(searchStr)) {
            return INDEX_NOT_FOUND;
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        final int endLimit = str.length() - searchStr.length() + 1;
        if (fromIndex > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return fromIndex;
        }
        if (!ignoreCase) {
            // 不忽略大小写调用JDK方法
            return str.toString().indexOf(searchStr.toString(), fromIndex);
        }
        for (int i = fromIndex; i < endLimit; i++) {
            if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return boolean
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (Objects.isNull(str1) || Objects.isNull(str2)) {
            return false;
        }
        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    /**
     * 重复字符串
     *
     * @param string 字符串
     * @param times  次数
     * @return String
     */
    public static String repeat(String string, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(string);
        }
        return sb.toString();
    }

    /**
     * 隐藏字符串
     *
     * @param param 字符串
     * @param start 前面显示位数
     * @param end   后面显示位数
     * @return String
     */
    public static String hideString(String param, int start, int end) {
        if (StringUtils.isBlank(param)) {
            return param;
        }
        if (param.length() <= start + end) {
            return param;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i < (param.length() - end); i++) {
            stringBuilder.append("*");
        }
        return param.substring(0, start) + stringBuilder.toString() + param.substring(param.length() - end);
    }

    /**
     * 整数补零
     * - 左边
     *
     * <pre>
     * eg:
     *     fillZeroLeft(5, 1)  ->  00001
     * </pre>
     *
     * @param len 长度
     * @param num 整数
     * @return String
     */
    public static String fillZeroLeft(int len, long num) {
        return String.format(StrUtil.format("%0{}d", len), num);
    }

    /**
     * 整数补零
     * - 右边
     *
     * <pre>
     * eg:
     *     fillZeroRight(5, 1)  ->  10000
     * </pre>
     *
     * @param len 长度
     * @param num 整数
     * @return String
     */
    public static String fillZeroRight(int len, long num) {
        return String.format(StrUtil.format("%-{}s", len), num).replace(" ", "0");
    }
}

package com.lmaye.cloud.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * -- 核心工具
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CoreUtils {
    /**
     * 校验包含大写字母的正则表达式
     */
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

    /**
     * 获取32位的UUID
     * - 去-符号
     *
     * @return String
     */
    public static String getUuid() {
        UUID object = UUID.randomUUID();
        String uuid = object.toString();
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }

    /**
     * 获取随机数(字母、数字)
     *
     * @param length 长度
     * @return String
     */
    public static String getRandomNumber(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取随机数(数字)
     *
     * @param length 长度
     * @return String
     */
    public static String getRandomNumberForNumbers(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 驼峰命名转下划线
     *
     * @param humpName 名称
     * @return String
     */
    public static String humpNameToUnderline(String humpName) {
        Matcher matcher = UPPERCASE_PATTERN.matcher(humpName);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取最大长度的字符串
     * - length - 3
     *
     * @param param  字符串
     * @param length 最大长度
     * @return String
     */
    public static String getMaxLengthString(String param, int length) {
        Objects.requireNonNull(param, "The intercepted string cannot be empty");
        if (param.length() > length) {
            return param.substring(0, length - 3) + "...";
        } else {
            return param;
        }
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
     * 金额大小写转换
     *
     * @param param 分
     * @return String
     */
    public static String digitUppercase(long param) {
        BigDecimal amount = new BigDecimal(param).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        String[] fraction = {"角", "分"};
        String[] digit = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[][] unit = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = amount.doubleValue() < 0 ? "负" : "";
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < fraction.length; i++) {
            double f1 = amount.multiply(BigDecimal.valueOf(10 * Math.pow(10, i))).doubleValue();
            s.append((digit[(int) (Math.floor(f1) % 10)] + fraction[i]).replaceAll("(零.)+", ""));
        }
        if (s.length() < 1) {
            s = new StringBuilder("整");
        }
        int integerPart = (int) Math.floor(amount.doubleValue());
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && amount.doubleValue() > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s.insert(0, p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]);
        }
        return head + s.toString().replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
                .replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }

    /**
     * 根据参数名获取参数值
     *
     * @param key 键
     * @param map Map集合
     * @return String
     */
    public static String getMapValue(String key, Map<String, Object> map) {
        if (Objects.isNull(map)) {
            return "";
        }
        Object mapValue = map.get(key);
        if (!Objects.isNull(mapValue)) {
            return mapValue.toString();
        }
        return "";
    }

    /**
     * 数据标识id部分
     *
     * @return long
     */
    public static long getDataCenterId() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (31 + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * byte数组转字符串
     *
     * @param buffer byte[]
     * @return String
     */
    public static String byteToStr(byte[] buffer) {
        try {
            int length = 0;
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(buffer, 0, length, "GBK");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes byte[]
     * @return String
     */
    public static String getCRC(byte[] bytes) {
        // initial value
        int crc = 0x00;
        int polynomial = 0x1021;
        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        return String.format("%04x", crc);
    }

    /**
     * 长整型转Hex并格式化
     *
     * @param number 长整型
     * @return String
     */
    public static String longToHex(long number) {
        return String.format("%016x", number);
    }

    /**
     * 本地时间转化为UTC时间
     *
     * @param localDate 本地时间
     * @return Date
     */
    public static Date localToUtc(Date localDate) {
        long localTimeInMillis = localDate.getTime();
        // long时间转换成Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        // 取得时间偏移量
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        // 从本地时间里扣除这些差量，即可以取得UTC时间
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        // 取得的时间就是UTC标准时间
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * Byte转bit
     *
     * @param b 字节
     * @return String
     */
    public static String byteTobit(byte b) {
        return String.valueOf((b >> 7) & 0x1) +
                ((b >> 6) & 0x1) +
                ((b >> 5) & 0x1) +
                ((b >> 4) & 0x1) +
                ((b >> 3) & 0x1) +
                ((b >> 2) & 0x1) +
                ((b >> 1) & 0x1) +
                (b & 0x1);
    }

    /**
     * Bit转byte
     *
     * @param bit bit
     * @return byte
     */
    public static byte bitToByte(String bit) {
        int re, len;
        if (null == bit) {
            return 0;
        }
        len = bit.length();
        if (len != 4 && len != 8) {
            return 0;
        }
        if (len == 8) {
            // 8 bit处理
            if (bit.charAt(0) == '0') {
                // 正数
                re = Integer.parseInt(bit, 2);
            } else {
                // 负数
                re = Integer.parseInt(bit, 2) - 256;
            }
        } else {
            //4 bit处理
            re = Integer.parseInt(bit, 2);
        }
        return (byte) re;
    }

    /**
     * 整形转字节数组
     *
     * @param num 整形
     * @return byte[]
     */
    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[4];
        //通过移位运算，截取低8位的方式，将int保存到byte数组
        bytes[0] = (byte) (num >>> 24);
        bytes[1] = (byte) (num >>> 16);
        bytes[2] = (byte) (num >>> 8);
        bytes[3] = (byte) num;
        return bytes;
    }

    /**
     * 字节数组转int
     *
     * @param bytes byte[]
     * @return int
     */
    public static int bytesToInt(byte[] bytes) {
        //如果不与0xff进行按位与操作，转换结果将出错，有兴趣的同学可以试一下。
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;
        return int1 | int2 | int3 | int4;
    }
}

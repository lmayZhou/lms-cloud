package com.lmaye.cloud.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * -- 身份证工具
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IDCardUtils {
    /**
     * 身份证号码中的出生日期的格式
     */
    public final static String BIRTH_DATE_FORMAT = "yyyyMMdd";

    /**
     * 身份证的最小出生日期,1900年1月1日
     */
    public final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L);
    public final static int NEW_CARD_NUMBER_LENGTH = 18;
    public final static int OLD_CARD_NUMBER_LENGTH = 15;

    /**
     * 18位身份证中最后一位校验码
     */
    public final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 18位身份证中，各个数字的生成校验码时的权值
     */
    public final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 校验身份证号码
     *
     * @param cardNumber 身份证号码
     * @return boolean
     */
    public static boolean validate(String cardNumber) {
        boolean result;
        // 身份证号不能为空
        result = null != cardNumber;
        // 身份证号长度是18(新证)
        result = result && NEW_CARD_NUMBER_LENGTH == cardNumber.length();
        // 身份证号的前17位必须是阿拉伯数字
        for (int i = 0; result && i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            result = ch >= '0' && ch <= '9';
        }
        // 身份证号的第18位校验正确
        result = result && (calculateVerifyCode(cardNumber) == cardNumber.charAt(NEW_CARD_NUMBER_LENGTH - 1));
        // 出生日期不能晚于当前时间，并且不能早于1900年
        try {
            Date birthDate = getBirthDate(cardNumber);
            result = result && null != birthDate;
            result = result && birthDate.before(new Date());
            result = result && birthDate.after(MINIMAL_BIRTH_DATE);
            /**
             * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
             * 月份和日期相符合
             */
            assert cardNumber != null;
            String birthdayPart = getBirthDayPart(cardNumber);
            String realBirthdayPart = createBirthDateParser().format(birthDate);
            result = result && (birthdayPart.equals(realBirthdayPart));
        } catch (Exception e) {
            result = false;
        }
        // TODO 完整身份证号码的省市县区检验规则
        return result;
    }

    /**
     * 获取地址编码
     *
     * @param cardNumber 身份证号
     * @return String
     */
    public static String getAddressCode(String cardNumber) {
        if (!validate(cardNumber)) {
            throw new RuntimeException("身份证号码不正确！");
        }
        return cardNumber.substring(0, 6);
    }

    /**
     * 获取出生日期
     *
     * @param cardNumber 身份证号
     * @return Date
     */
    public static Date getBirthDate(String cardNumber) {
        try {
            return createBirthDateParser().parse(getBirthDayPart(cardNumber));
        } catch (Exception e) {
            throw new RuntimeException("身份证的出生日期无效");
        }
    }

    /**
     * 男
     *
     * @param cardNumber 身份证号
     * @return boolean
     */
    public static boolean isMale(String cardNumber) {
        return (getGenderCode(cardNumber) & 1) != 0;
    }

    /**
     * 女
     *
     * @param cardNumber 身份证号
     * @return boolean
     */
    public static boolean isFemale(String cardNumber) {
        return !isMale(cardNumber);
    }

    /**
     * 获取身份证的第17位，奇数为男性，偶数为女性
     *
     * @param cardNumber 身份证号
     * @return int
     */
    public static int getGenderCode(String cardNumber) {
        if (!validate(cardNumber)) {
            throw new RuntimeException("身份证号码不正确！");
        }
        char genderCode = cardNumber.charAt(NEW_CARD_NUMBER_LENGTH - 2);
        return ((genderCode - '0') & 0x1);
    }

    /**
     * 获取出生日期字符串
     *
     * @param cardNumber 身份证号
     * @return String
     */
    public static String getBirthDayPart(String cardNumber) {
        return cardNumber.substring(6, 14);
    }

    /**
     * 格式化出生日期
     *
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat createBirthDateParser() {
        return new SimpleDateFormat(BIRTH_DATE_FORMAT);
    }

    /**
     * <li>校验码（第十八位数）：<br/>
     * <ul>
     * <li>十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
     * 2；</li>
     * <li>计算模 Y = mod(S, 11)</li>
     * <li>通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2</li>
     * </ul>
     *
     * @param cardNumber 身份证号码
     * @return char
     */
    public static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
            char ch = cardNumber.charAt(i);
            sum += (ch - '0') * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[sum % 11];
    }

    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
     *
     * @param cardNumber 身份证号码
     * @return String
     */
    public static String convertToNewCardNumber(String cardNumber) {
        StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
        buf.append(cardNumber, 0, 6);
        buf.append("19");
        buf.append(cardNumber.substring(6));
        buf.append(IDCardUtils.calculateVerifyCode(buf));
        return buf.toString();
    }

    /**
     * 根据年月日计算年龄
     *
     * @param birthDate 日期
     * @return int
     */
    public static int getAgeFromBirthTime(Date birthDate) {
        // 先截取到字符串中的年、月、日
        String[] array = DateUtils.format(birthDate, "yyyy-MM-dd").trim().split("-");
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        // 用当前年月日减去生日年月日
        int yearMinus = cal.get(Calendar.YEAR) - Integer.parseInt(array[0]);
        int monthMinus = cal.get(Calendar.MONTH) + 1 - Integer.parseInt(array[1]);
        int dayMinus = cal.get(Calendar.DATE) - Integer.parseInt(array[2]);
        int age = yearMinus;
        if (yearMinus <= 0) {
            // 选了未来的年份
            age = 0;
        } else {
            if (monthMinus == 0) {
                // 同月份的，再根据日期计算年龄
                if (dayMinus >= 0) {
                    age = age + 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

    /**
     * 根据时间戳计算年龄
     *
     * @param birthTimeLong 时间戳
     * @return int
     */
    public static int getAgeFromBirthTime(long birthTimeLong) {
        return getAgeFromBirthTime(new Date(birthTimeLong * 1000L));
    }
}

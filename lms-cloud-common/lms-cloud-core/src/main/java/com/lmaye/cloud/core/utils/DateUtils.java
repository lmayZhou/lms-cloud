package com.lmaye.cloud.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * -- 日期工具
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {
    /**
     * 开始时间范围后缀
     */
    public static final String TIME_START_POSTFIX = "00:00:00.000";
    /**
     * 结束时间范围后缀
     */
    public static final String TIME_END_POSTFIX = "23:59:59.999";
    private static final String[] ZODIAC_ARR = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final String[] CONSTELLATION_ARR = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] CONSTELLATION_EDGE_DAY = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    /**
     * 日期正则表达式
     */
    public static String YEAR_REGEX = "^\\d{4}$";
    public static String MONTH_REGEX = "^\\d{4}([-/.])\\d{1,2}$";
    public static String DATE_REGEX = "^\\d{4}([-/.])\\d{1,2}\\1\\d{1,2}$";

    /**
     * 根据日期获取生肖
     *
     * @param date 日期
     * @return String
     */
    public static String getZodica(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return ZODIAC_ARR[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据日期获取星座
     *
     * @param date 日期
     * @return String
     */
    public static String getConstellation(Date date) {
        Objects.requireNonNull(date, "日期不能为空");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATION_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return CONSTELLATION_ARR[month];
        }
        return CONSTELLATION_ARR[11];
    }

    /**
     * 获取当天的开始时间
     * - 格式: yyyy-MM-dd HH:mm:ss
     *
     * @return Date
     */
    public static Date getDayBegin() {
        Date date = new Date();
        return getDayStartTime(date);
    }

    /**
     * 获取当天的结束时间
     *
     * @return Date
     */
    public static Date getDayEnd() {
        Date date = new Date();
        return getDayEndTime(date);
    }

    /**
     * 获取昨天的开始时间
     *
     * @return Date
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取昨天的结束时间
     *
     * @return Date
     */
    public static Date getEndDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取明天的开始时间
     *
     * @return Date
     */
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取明天的结束时间
     *
     * @return Date
     */
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取本周的开始时间
     *
     * @return Date
     */
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     *
     * @return Date
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     *
     * @return Date
     */
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     *
     * @return Date
     */
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本年的开始时间
     *
     * @return Date
     */
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本年的结束时间
     *
     * @return Date
     */
    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param date 日期
     * @return Date
     */
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param date 日期
     * @return Date
     */
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某年某月的第一天
     *
     * @param year  年
     * @param month 月
     * @return Date
     */
    public static Date getStartMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getTime();
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return Date
     */
    public static Date getEndMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int day = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取今年是哪一年
     *
     * @return Integer
     */
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.YEAR);
    }

    /**
     * 获取本月是哪一月
     *
     * @return int
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某个日期是哪一月
     *
     * @param date 日期
     * @return int
     */
    public static int getWhichMonth(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 两个日期相减得到的天数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return int
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);
        return (int) diff;
    }

    /**
     * 两个日期相减得到的毫秒数
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return long
     */
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    /**
     * 获取两个日期中的最大日期
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return Date
     */
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    /**
     * 获取两个日期中的最小日期
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return Date
     */
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    /**
     * 返回某月该季度的第一个月
     *
     * @param date 日期
     * @return Date
     */
    public static Date getFirstSeasonDate(Date date) {
        final int[] season = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = season[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几个小时的日期
     *
     * @param date 日期
     * @param t    变数
     * @return Date
     */
    public static Date getNextHour(Date date, int t) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, t);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几天的日期
     *
     * @param date 日期
     * @param t    变数
     * @return Date
     */
    public static Date getNextDay(Date date, int t) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, t);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几月的日期
     *
     * @param date 日期
     * @param t    变数
     * @return Date
     */
    public static Date getNextMonth(Date date, int t) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, t);
        return cal.getTime();
    }

    /**
     * 返回某个日期前几月的日期
     *
     * @param date 日期
     * @param t    变数
     * @return Date
     */
    public static Date getFrontMonth(Date date, int t) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -t);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几年的日期
     *
     * @param date 日期
     * @param t    变数
     * @return Date
     */
    public static Date getNextYear(Date date, int t) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.YEAR, t);
        return cal.getTime();
    }

    /**
     * 获取某年某月到某年某月按天的切片日期集合（间隔天数的日期集合）
     *
     * @param beginYear  开始年份
     * @param beginMonth 开始月份
     * @param endYear    结束年份
     * @param endMonth   结束月份
     * @param t          间隔天数
     * @return List<List < Date>>
     */
    public static List<List<Date>> getTimeList(int beginYear, int beginMonth, int endYear, int endMonth, int t) {
        List<List<Date>> list = new ArrayList<>();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, t));
            }
        } else {
            int monthNum = 12;
            for (int j = beginMonth; j < monthNum; j++) {
                list.add(getTimeList(beginYear, j, t));
            }
            for (int i = beginYear + 1; i < endYear; i++) {
                for (int j = 0; j < monthNum; j++) {
                    list.add(getTimeList(i, j, t));
                }
            }
            for (int j = 0; j <= endMonth; j++) {
                list.add(getTimeList(endYear, j, t));
            }
        }
        return list;
    }

    /**
     * 获取某年某月按天切片日期集合（某个月间隔多少天的日期集合）
     *
     * @param beginYear  开始年份
     * @param beginMonth 开始月份
     * @param t          间隔天数
     * @return List<Date>
     */
    public static List<Date> getTimeList(int beginYear, int beginMonth, int t) {
        List<Date> list = new ArrayList<>();
        Calendar beginCal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = beginCal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + t) {
            list.add(beginCal.getTime());
            beginCal.add(Calendar.DATE, t);
        }
        beginCal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(beginCal.getTime());
        return list;
    }

    /**
     * 获取某个日期是哪一天
     *
     * @param date 日期
     * @return int
     */
    public static int getWhichDay(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.DATE);
    }

    /**
     * 获取某个日期是周几
     *
     * @param date 日期
     * @return int
     */
    public static int getWhichWeek(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        int dayForWeek;
        if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = gc.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 格式化日期
     * - eg: yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @param pattern 格式
     * @return String
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        return sd.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        return sd.parse(date);
    }

    /**
     * 获取当前时间Time Stamp
     *
     * @param pattern 日期格式
     * @return String
     */
    public static String getCurrentTimeStamp(String pattern) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return format(ts, pattern);
    }

    /**
     * 获取Unix时间戳
     *
     * @param date 日期
     * @return String
     */
    public static String date2TimeStamp(Date date) {
        return String.valueOf(date.getTime() / 1000);
    }

    /**
     * 时间范围 - 切片
     * <pre>
     * - eg:
     * ----------------------- sliceUpDateRange("2018", "2020");
     *  {date=2018, nextDate=2019}
     *  {date=2019, nextDate=2020}
     *  {date=2020, nextDate=2021}
     *
     * ----------------------- sliceUpDateRange("2018-06", "2018-08");
     *  {date=2018-06, nextDate=2018-07}
     *  {date=2018-07, nextDate=2018-08}
     *  {date=2018-08, nextDate=2018-09}
     *
     * ----------------------- sliceUpDateRange("2018-06-30", "2018-07-02");
     *  {date=2018-06-30, nextDate=2018-07-01}
     *  {date=2018-07-01, nextDate=2018-07-02}
     *  {date=2018-07-02, nextDate=2018-07-03}
     * </pre>
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 切片日期 {date, nextDate}
     */
    public static List<Map<String, String>> sliceUpDateRange(String startDate, String endDate) {
        List<Map<String, String>> rs = new ArrayList<>();
        try {
            int dt = Calendar.DATE;
            String pattern = "yyyy-MM-dd";
            if (startDate.matches(YEAR_REGEX)) {
                pattern = "yyyy";
                dt = Calendar.YEAR;
            } else if (startDate.matches(MONTH_REGEX)) {
                pattern = "yyyy-MM";
                dt = Calendar.MONTH;
            } else if (startDate.matches(DATE_REGEX)) {
                pattern = "yyyy-MM-dd";
                dt = Calendar.DATE;
            }
            Calendar sc = Calendar.getInstance();
            Calendar ec = Calendar.getInstance();
            sc.setTime(parse(startDate, pattern));
            ec.setTime(parse(endDate, pattern));
            while (sc.compareTo(ec) < 1) {
                Map<String, String> map = new HashMap<>(2);
                map.put("date", format(sc.getTime(), pattern));
                sc.add(dt, 1);
                map.put("nextDate", format(sc.getTime(), pattern));
                rs.add(map);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }
}

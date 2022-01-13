package com.lmaye.cloud.starter.serialno.strategy;

/**
 * -- Serial No Strategy
 *
 * @author Lmay Zhou
 * @date 2022/1/5 10:08
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public interface ISerialNoStrategy {
    /**
     * 生成业务序号
     *
     * @param businessLogo 业务标识
     * @param globalId     Redis 全局ID
     * @param delimiter    分隔符(默认无)
     * @param dateFormat   日期格式
     * @return String
     */
    String generate(String businessLogo, String globalId, String delimiter, String dateFormat);

    /**
     * 格式化业务序号
     *
     * @param businessLogo 业务标识
     * @param serialNo     业务序号
     * @param delimiter    分隔符
     * @param dateFormat   日期格式
     * @return String
     */
    String format(String businessLogo, String serialNo, String delimiter, String dateFormat);
}

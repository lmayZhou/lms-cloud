package com.lmaye.cloud.starter.serialno.pattern;

import com.lmaye.cloud.starter.serialno.strategy.ISerialNoStrategy;

/**
 * -- Serial No Context
 *
 * @author Lmay Zhou
 * @date 2022/1/5 17:28
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public class SerialNoContext {
    /**
     * Serial No Strategy
     */
    private ISerialNoStrategy serialNoStrategy;

    public void setStrategy(ISerialNoStrategy serialNoStrategy) {
        this.serialNoStrategy = serialNoStrategy;
    }

    /**
     * 生成业务序号
     *
     * @param businessLogo 业务标识
     * @param globalId     Redis 全局ID
     * @param delimiter    分隔符(默认无)
     * @param dateFormat   日期格式
     * @return String
     */
    public String generate(String businessLogo, String globalId, String delimiter, String dateFormat) {
        return serialNoStrategy.generate(businessLogo, globalId, delimiter, dateFormat);
    }

    /**
     * 格式化业务序号
     *
     * @param businessLogo 业务标识
     * @param serialNo     业务序号
     * @param delimiter    分隔符
     * @param dateFormat   日期格式
     * @return String
     */
    public String format(String businessLogo, String serialNo, String delimiter, String dateFormat) {
        return serialNoStrategy.format(businessLogo, serialNo, delimiter, dateFormat);
    }
}

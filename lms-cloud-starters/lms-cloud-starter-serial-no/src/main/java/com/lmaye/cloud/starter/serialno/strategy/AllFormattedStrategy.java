package com.lmaye.cloud.starter.serialno.strategy;

import com.lmaye.cloud.core.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * -- All Formatted Strategy
 *
 * <pre>
 * 示例:
 *     1001-220104-00000001      全部、已分隔(20)
 * </pre>
 *
 * @author Lmay Zhou
 * @date 2022/1/5 10:54
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Component
public class AllFormattedStrategy implements ISerialNoStrategy {
    /**
     * 生成业务序号
     *
     * @param businessLogo 业务标识
     * @param globalId     Redis 全局ID
     * @param delimiter    分隔符(默认无)
     * @param dateFormat   日期格式
     * @return String
     */
    @Override
    public String generate(String businessLogo, String globalId, String delimiter, String dateFormat) {
        return businessLogo + delimiter + DateUtils.format(new Date(), dateFormat) + delimiter + globalId;
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
    @Override
    public String format(String businessLogo, String serialNo, String delimiter, String dateFormat) {
        return serialNo;
    }
}

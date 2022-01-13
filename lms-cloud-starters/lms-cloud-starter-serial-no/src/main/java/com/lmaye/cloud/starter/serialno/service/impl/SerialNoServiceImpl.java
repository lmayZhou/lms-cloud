package com.lmaye.cloud.starter.serialno.service.impl;

import com.lmaye.cloud.core.utils.DateUtils;
import com.lmaye.cloud.core.utils.StringCoreUtils;
import com.lmaye.cloud.starter.serialno.SerialNoProperties;
import com.lmaye.cloud.starter.serialno.pattern.SerialNoContext;
import com.lmaye.cloud.starter.serialno.pattern.SerialNoFactory;
import com.lmaye.cloud.starter.serialno.service.ISerialNoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * -- Serial Number Service
 *
 * @author Lmay Zhou
 * @date 2022/1/4 10:35
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Service
public class SerialNoServiceImpl implements ISerialNoService {
    /**
     * Redisson Client
     */
    @Autowired
    private RedissonClient redissonClient;

    /**
     * Serial No Properties
     */
    @Autowired
    private SerialNoProperties serialNoProperties;

    /**
     * 生成业务序号
     *
     * <pre>
     * 示例
     * 字节长度    4         6          8
     * 序号解析  业务标识 + 年月日    + Redis全局ID
     *          1001      220104     00000001
     *      未格式化
     *      eg: 00000001                  Redis全局ID(8)
     *      eg: 100100000001              业务标识 + Redis全局ID(12)
     *      eg: 22010400000001            日期 + Redis全局ID(14)
     *      eg: 100122010400000001        业务标识 + 日期 + Redis全局ID(18)
     *      已格式化
     *      eg: 1001-220104-00000001      业务标识 + 日期 + Redis全局ID(20)
     *      eg: 220104-00000001           日期 + Redis全局ID(15)
     *      eg: 1001-00000001             业务标识 + Redis全局ID(13)
     * </pre>
     *
     * @param businessLogo 业务标识
     * @param delimiter    分隔符(默认无)
     * @param hasDate      是否带有日期(默认无)
     * @return String
     */
    @Override
    public String generate(String businessLogo, String delimiter, boolean hasDate) {
        final RAtomicLong atomicLong = redissonClient.getAtomicLong("GlobalIncrId:" + businessLogo);
        final long incrId = atomicLong.incrementAndGet();
        atomicLong.expire(DateUtils.getDayEnd().toInstant());
        SerialNoContext serialNoContext = new SerialNoContext();
        serialNoContext.setStrategy(SerialNoFactory.getPattern(businessLogo, delimiter, hasDate));
        return serialNoContext.generate(businessLogo, StringCoreUtils.fillZero(serialNoProperties.getGlobalIdLen(),
                incrId), delimiter, serialNoProperties.getDateFormat());
    }

    /**
     * 格式化业务序号
     *
     * <pre>
     * 示例
     *      eg: 100100000001        ->  1001-00000001
     *      eg: 22010400000001      ->  220104-00000001
     *      eg: 100122010400000001  ->  1001-220104-00000001
     * </pre>
     *
     * @param businessLogo 业务标识
     * @param serialNo     业务序号
     * @param delimiter    分隔符
     * @return String
     */
    @Override
    public String format(String businessLogo, String serialNo, String delimiter) {
        final String dateFormat = serialNoProperties.getDateFormat();
        SerialNoContext serialNoContext = new SerialNoContext();
        serialNoContext.setStrategy(SerialNoFactory.getPattern(businessLogo, serialNo, serialNoProperties.getGlobalIdLen(),
                dateFormat.length()));
        return serialNoContext.format(businessLogo, serialNo, delimiter, dateFormat);
    }
}

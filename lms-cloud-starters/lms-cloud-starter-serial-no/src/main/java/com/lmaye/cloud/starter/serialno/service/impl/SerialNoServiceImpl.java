package com.lmaye.cloud.starter.serialno.service.impl;

import com.lmaye.cloud.core.utils.DateUtils;
import com.lmaye.cloud.core.utils.CoreUtils;
import com.lmaye.cloud.starter.serialno.SerialNoProperties;
import com.lmaye.cloud.starter.serialno.pattern.SerialNoContext;
import com.lmaye.cloud.starter.serialno.pattern.SerialNoFactory;
import com.lmaye.cloud.starter.serialno.service.ISerialNoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RList;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

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
    @Resource
    private RedissonClient redissonClient;

    /**
     * Serial No Properties
     */
    @Resource
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
        final String globalId;
        final int globalIdLen = serialNoProperties.getGlobalIdLen();
        if (serialNoProperties.getIsOrderly()) {
            final RAtomicLong atomicLong = redissonClient.getAtomicLong("GlobalIncrId:" + businessLogo);
            final long incrId = atomicLong.incrementAndGet();
            if (serialNoProperties.getIsExpire()) {
                atomicLong.expire(DateUtils.getDayEnd().toInstant());
            }
            globalId = CoreUtils.fillZeroLeft(globalIdLen, incrId);
        } else {
            final String startNo = CoreUtils.fillNumRight(globalIdLen, 1, "0");
            final String key = "GlobalRandomIncr:" + businessLogo;
            final RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            if (serialNoProperties.getIsExpire()) {
                atomicLong.expire(DateUtils.getDayEnd().toInstant());
            }
            if (Objects.equals(0L, redissonClient.getKeys().countExists(key))) {
                atomicLong.set(Integer.parseInt(startNo) - 1);
            }
            final Long incr = redissonClient.getScript(LongCodec.INSTANCE).eval(RScript.Mode.READ_WRITE,
                    "local key = KEYS[1];" +
                            "local incr = redis.call('incr', key);" +
                            "if incr >= tonumber(ARGV[1]) then " +
                            "    redis.call('set', key, tonumber(ARGV[2]));" +
                            "    return incr;" +
                            "end;" +
                            "return incr;", RScript.ReturnType.INTEGER, Collections.singletonList(key),
                    CoreUtils.fillNumRight(globalIdLen, 9, "9"), startNo);
            final String incrStr = String.valueOf(incr);
            final RList<Integer> rList = redissonClient.getList("GlobalRandomNo:" + incrStr.substring(0, 2));
            globalId = String.valueOf(rList.get(Integer.parseInt(incrStr.substring(2))));
        }
        SerialNoContext serialNoContext = new SerialNoContext();
        serialNoContext.setStrategy(SerialNoFactory.getPattern(businessLogo, delimiter, hasDate));
        return serialNoContext.generate(businessLogo, globalId, delimiter, serialNoProperties.getDateFormat());
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

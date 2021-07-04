package com.lmaye.cloud.starter.canal.properties;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * -- CanalProperties
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Data
public class CanalProperties {
    public static final String CANAL_PREFIX = "canal";

    public static final String CANAL_ASYNC = CANAL_PREFIX + "." + "async";

    public static final String CANAL_MODE = CANAL_PREFIX + "." + "mode";

    /**
     * simple, cluster, zookeeper, kafka, rocketMQ
     */
    private String mode;

    private Boolean async;

    private String server;

    private String destination;

    private String filter = StringUtils.EMPTY;

    private Integer batchSize = 1;

    private Long timeout = 1L;

    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 是否开启
     */
    private Boolean enable = true;
}

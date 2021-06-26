package com.lmaye.cloud.core.context;

/**
 * -- 响应编码
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @since Email: lmay_zlm@meten.com
 */
public interface IResultCode {
    /**
     * 获取代码
     *
     * @return Integer
     */
    Integer getCode();

    /**
     * 获取Key
     *
     * @return String
     */
    String getKey();

    /**
     * 获取消息
     *
     * @return String
     */
    String getDesc();
}

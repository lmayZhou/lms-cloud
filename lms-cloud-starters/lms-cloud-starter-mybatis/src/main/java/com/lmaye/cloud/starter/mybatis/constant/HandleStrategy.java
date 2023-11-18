package com.lmaye.cloud.starter.mybatis.constant;

/**
 * -- HandleStrategy
 *
 * @author Lmay Zhou
 * @date 2023/11/16 19:36
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public enum HandleStrategy {
    /**
     * 枚举对象
     * <pre>
     *     数据加解密时策略
     *     ----------------------------
     *     SAVE   存储(存储策略: 响应解密)
     *     RESULT 响应(响应策略: 存储解密)
     * </pre>
     */
    SAVE,
    RESULT
}

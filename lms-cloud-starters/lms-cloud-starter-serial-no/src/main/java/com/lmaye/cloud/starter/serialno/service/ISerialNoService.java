package com.lmaye.cloud.starter.serialno.service;

/**
 * -- Serial Number Service
 *
 * @author Lmay Zhou
 * @date 2022/1/4 10:34
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public interface ISerialNoService {
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
    String generate(String businessLogo, String delimiter, boolean hasDate);

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
    String format(String businessLogo, String serialNo, String delimiter);
}

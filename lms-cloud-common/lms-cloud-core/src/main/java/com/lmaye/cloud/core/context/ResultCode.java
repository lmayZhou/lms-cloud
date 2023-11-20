package com.lmaye.cloud.core.context;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * -- 响应编码
 * - 枚举
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    /**
     * 枚举对象
     */
    SUCCESS(200, "msg.success", "请求成功"),
    FAILURE(-100, "msg.failure", "请求失败"),
    FLOW_LIMITING(-130, "msg.flow.limiting", "流量限制"),
    ARGUMENT_BIND_FAILED(-131, "msg.argument.bind.failed", "参数绑定失败"),
    ANTISAMY_DATA_INIT_FAILED(-132, "msg.antisamy.data.init.failed", "antisamy数据初始化失败"),
    JSON_BEAN_TO_STR_FAILED(-133, "msg.json.bean.to.str.failed", "Json转字符串失败"),
    JSON_STR_TO_BEAN_FAILED(-134, "msg.json.str.to.bean.failed", "字符串转Json失败"),
    IP_DATA_INIT_FAILED(-135, "msg.ip.data.init.failed", "IP初始化失败"),
    GET_IP_ADDRESS_FAILED(-136, "msg.get.ip.address.failed", "获取IP地址失败"),
    RECORD_NOT_EXIST(-137, "msg.record.not.exist", "记录不存在"),
    UNAUTHORIZED(401, "msg.unauthorized", "未经授权"),
    FORBIDDEN(403, "msg.forbidden", "拒绝访问"),
    NOT_FOUND(404, "msg.not.found", "未找到"),
    METHOD_NOT_ALLOWED(405, "msg.method.not.allowed", "方法禁用"),
    INTERNAL_SERVER_ERROR(500, "msg.internal.server.error", "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "msg.service.unavailable", "服务不可用"),
    REQUIRED_ID(-1001, "msg.required.id", "主键[id]不允许为空");

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 键
     */
    private final String key;

    /**
     * 描述
     */
    private final String desc;
}

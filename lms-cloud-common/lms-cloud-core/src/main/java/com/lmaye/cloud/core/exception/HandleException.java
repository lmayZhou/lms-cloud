package com.lmaye.cloud.core.exception;

import com.lmaye.cloud.core.context.IResultCode;

/**
 * -- 处理自定义异常
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/7/1 8:03 星期三
 */
public class HandleException extends RuntimeException {
    /**
     * 响应编码
     */
    private final IResultCode resultCode;

    public HandleException(IResultCode resultCode) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
    }

    public HandleException(IResultCode resultCode, Throwable cause) {
        super(resultCode.getDesc(), cause);
        this.resultCode = resultCode;
    }

    /**
     * 获取错误信息
     *
     * @return IResultCode
     */
    public IResultCode getResultCode() {
        return resultCode;
    }
}

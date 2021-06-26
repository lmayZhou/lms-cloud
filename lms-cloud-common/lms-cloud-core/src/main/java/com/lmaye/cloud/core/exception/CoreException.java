package com.lmaye.cloud.core.exception;

import com.lmaye.cloud.core.context.IResultCode;

/**
 * -- 通用自定义异常
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
public class CoreException extends RuntimeException {
    /**
     * 响应编码
     */
    private final IResultCode resultCode;

    public CoreException(IResultCode resultCode) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
    }

    public CoreException(IResultCode resultCode, Throwable cause) {
        super(resultCode.getDesc(), cause);
        this.resultCode = resultCode;
    }

    /**
     * 获取错误信息
     *
     * @return ResultCode
     */
    public IResultCode getResultCode() {
        return resultCode;
    }
}

package com.lmaye.cloud.core.exception;

import com.lmaye.cloud.core.context.IResultCode;

/**
 * -- 业务自定义异常
 *
 * @author lmay.Zhou
 * @date 2020/12/1 15:35
 * @email lmay@lmaye.com
 */
public class ServiceException extends RuntimeException {
    /**
     * 响应编码
     */
    private final IResultCode resultCode;

    public ServiceException(IResultCode resultCode) {
        super(resultCode.getDesc());
        this.resultCode = resultCode;
    }

    public ServiceException(IResultCode resultCode, Throwable cause) {
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

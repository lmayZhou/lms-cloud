package com.lmaye.cloud.starter.web.context;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lmaye.cloud.core.context.IResultCode;
import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.starter.web.WebProperties;
import com.lmaye.cloud.starter.web.utils.SpringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * -- 通用响应结果实体
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ResultVO", description = "响应结果")
public class ResultVO<T> implements Serializable {
    /**
     * 响应代码
     */
    @ApiModelProperty("响应代码")
    private Integer code;

    /**
     * 响应消息
     */
    @ApiModelProperty("响应消息")
    private String msg;

    /**
     * 响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVO(IResultCode resultCode) {
        initParams(resultCode);
    }

    public ResultVO(IResultCode resultCode, T data) {
        initParams(resultCode);
        this.data = data;
    }

    /**
     * 初始化字段参数
     *
     * <pre>
     *  自定义响应枚举对象:
     *     public enum XXXCode implements IResultCode {
     *                 状态码    消息Key(I18n)     响应描述
     *         SUCCESS(200,    "msg.success",    "请求成功"),
     *      ...
     *     }
     *
     *  国际化文件定义格式:
     *     msg.success=Success
     *     msg.failure=Failure
     * </pre>
     * @see ResultCode
     * @param resultCode IResultCode
     */
    private void initParams(IResultCode resultCode) {
        this.code = resultCode.getCode();
        // 通过 Spring ApplicationContext 获取对象实例
        WebProperties webProperties = SpringUtils.getBean("webProperties");
        // 消息Key(eg: msg.success)
        String key = resultCode.getKey();
        this.msg = StringUtils.isBlank(key) || !webProperties.getI18n().getEnabled() ? resultCode.getDesc() : key;
    }

    /**
     * 处理成功
     *
     * @param data 响应数据
     * @param <T>  泛型
     * @return ResponseResult<T>
     */
    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(ResultCode.SUCCESS, data);
    }

    /**
     * 处理失败
     *
     * @param <T> 泛型
     * @return ResponseResult<T>
     */
    public static <T> ResultVO<T> failed() {
        return new ResultVO<>(ResultCode.FAILURE, null);
    }

    /**
     * 响应结果
     *
     * @param resultCode 响应编码
     * @param data       响应数据
     * @param <T>        泛型
     * @return ResponseResult<T>
     */
    public static <T> ResultVO<T> response(IResultCode resultCode, T data) {
        return new ResultVO<>(resultCode, data);
    }

    /**
     * 响应结果是否成功
     *
     * @return boolean
     */
    public boolean isSuccess() {
        if (Objects.isNull(code)) {
            return false;
        }
        return Objects.equals(ResultCode.SUCCESS.getCode(), code);
    }
}

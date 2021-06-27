package com.lmaye.cloud.starter.mybatis.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * -- 加密类型
 *
 * @author lmay.Zhou
 * @date 2019/12/9 10:30 星期一
 * @email lmay@lmaye.com
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EncryptType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 初始值
     */
    private String val;

    /**
     * 显示值
     */
    private String showVal;

    /**
     * 加密后的值
     */
    @JsonIgnore
    private String encryptVal;

    /**
     * 只要val为空则表示对象为空
     *
     * @return boolean
     */
    @JsonIgnore
    public boolean isNull() {
        return Objects.isNull(val);
    }
}

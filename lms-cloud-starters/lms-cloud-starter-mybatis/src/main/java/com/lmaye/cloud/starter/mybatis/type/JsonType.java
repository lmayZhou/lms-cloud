package com.lmaye.cloud.starter.mybatis.type;

import com.lmaye.cloud.starter.web.utils.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- JSON 类型
 *
 * @author lmay.Zhou
 * @date 2019/12/6 18:30 星期五
 * @email lmay@lmaye.com
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JsonType implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 原字符串
     */
    private String val;

    /**
     * JSON 字符串转对象
     *
     * @param clazz Class
     * @param <T>   泛型
     * @return T
     */
    public <T> T toBean(Class<T> clazz) {
        return JsonUtils.toBean(val, clazz);
    }
}

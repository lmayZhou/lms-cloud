package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * -- 范围查询参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Tag(name = "RangeQuery", description = "范围查询参数")
public class RangeQuery extends Negation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段
     */
    @Safe
    @NotBlank
    @Schema(description = "字段")
    private String field;

    /**
     * 小值
     */
    @NotNull
    @Schema(description = "小值")
    private Object le;

    /**
     * 大值
     */
    @NotNull
    @Schema(description = "大值")
    private Object ge;
}

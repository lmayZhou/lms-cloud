package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * -- 模糊查询参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "MatchQuery", description = "模糊查询参数")
public class MatchQuery extends Negation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段
     */
    @Safe
    @NotBlank
    @ApiModelProperty("字段")
    private String field;

    /**
     * 值
     */
    @NotNull
    @ApiModelProperty("值")
    private Object value;
}

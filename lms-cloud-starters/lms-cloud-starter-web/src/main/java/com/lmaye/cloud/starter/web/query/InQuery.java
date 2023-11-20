package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * -- IN查询参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "InQuery", description = "IN查询参数")
public class InQuery extends Negation implements Serializable {
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
    @NotEmpty
    @ApiModelProperty("值")
    private List<Object> values;
}

package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Tag(name = "InQuery", description = "IN查询参数")
public class InQuery extends Negation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字段
     */
    @Safe
    @NotBlank
    @Schema(description = "字段")
    private String field;

    /**
     * 值
     */
    @NotEmpty
    @Schema(description = "值")
    private List<Object> values;
}

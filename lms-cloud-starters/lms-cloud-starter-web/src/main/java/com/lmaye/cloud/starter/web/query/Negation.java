package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.core.constants.YesOrNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Objects;

/**
 * -- 是否否定
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@ApiModel(value = "Negation", description = "是否否定")
public class Negation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否否定: 0. 否; 1. 是;
     */
    @Range(min = 0, max = 1)
    @ApiModelProperty("是否否定: 0. 否; 1. 是;")
    private Integer negation = 0;

    /**
     * 是否否定
     *
     * @return boolean
     */
    public boolean isNegation() {
        return Objects.equals(YesOrNo.YES.getCode(), negation);
    }
}

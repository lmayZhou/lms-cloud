package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * -- Order
 *
 * @author lmay.Zhou
 * @date 2020-12-01 15:23:22
 * @since Email: lmay_zlm@meten.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Order", description = "顺序参数")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    @Safe
    @NotBlank
    @ApiModelProperty("排序字段")
    private String name;

    /**
     * 是否正序: 0. 否; 1. 是;
     */
    @NotNull
    @Range(min = 0, max = 1)
    @ApiModelProperty("是否正序: 0. 否; 1. 是;")
    private Integer asc;
}

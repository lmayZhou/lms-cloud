package com.lmaye.cloud.starter.web.query;

import com.lmaye.cloud.starter.web.validator.constraints.Safe;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Tag(name = "Order", description = "顺序参数")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序字段
     */
    @Safe
    @NotBlank
    @Schema(description = "排序字段")
    private String name;

    /**
     * 是否正序: 0. 否; 1. 是;
     */
    @NotNull
    @Range(min = 0, max = 1)
    @Schema(description = "是否正序: 0. 否; 1. 是;")
    private Integer asc;
}

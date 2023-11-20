package com.lmaye.cloud.starter.web.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * -- 排序参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Sort", description = "排序参数")
public class Sort implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    @NotEmpty
    private List<Order> order;
}

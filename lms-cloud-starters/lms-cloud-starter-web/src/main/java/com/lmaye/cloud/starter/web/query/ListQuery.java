package com.lmaye.cloud.starter.web.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * -- 列表查询参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@Builder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ListQuery", description = "列表查询参数")
public class ListQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 条件
     */
    @Valid
    @ApiModelProperty("条件")
    private Query query;

    /**
     * 排序
     */
    @Valid
    @ApiModelProperty("排序")
    private Sort sort;
}

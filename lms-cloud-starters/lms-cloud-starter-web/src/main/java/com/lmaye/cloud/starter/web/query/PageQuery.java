package com.lmaye.cloud.starter.web.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * -- 分页查询参数
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020/12/1 14:34
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PageQuery", description = "分页查询参数")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 查询条件
     */
    @ApiModelProperty("查询条件")
    @Valid
    private Query query;

    /**
     * 当前页码(默认: 1)
     */
    @Min(1)
    @ApiModelProperty("当前页码(默认: 1)")
    private Long pageIndex = 1L;

    /**
     * 每页显示页数(默认: 10)
     */
    @Range(min = 1, max = 10000)
    @ApiModelProperty("每页显示页数(默认: 10)")
    private Long pageSize = 10L;

    /**
     * 排序
     */
    @Valid
    @ApiModelProperty("排序")
    private Sort sort;

    /**
     * 滚动ID(深度分页查询)
     */
    @ApiModelProperty("滚动ID(深度分页查询)")
    private String scrollId;
}

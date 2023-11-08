package com.lmaye.cloud.starter.web.query;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "PageQuery", description = "分页查询参数")
public class PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 查询条件
     */
    @Valid
    @Schema(description = "查询条件")
    private Query query;

    /**
     * 当前页码(默认: 1)
     */
    @Min(1)
    @Schema(description = "当前页码(默认: 1)", example = "1")
    private Long pageIndex = 1L;

    /**
     * 每页显示页数(默认: 10)
     */
    @Range(min = 1, max = 10000)
    @Schema(description = "每页显示页数(默认: 10)", example = "10")
    private Long pageSize = 10L;

    /**
     * 排序
     */
    @Valid
    @Schema(description = "排序")
    private Sort sort;

    /**
     * 滚动ID(深度分页查询)
     */
    @Schema(description = "滚动ID(深度分页查询)")
    private String scrollId;
}

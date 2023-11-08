package com.lmaye.cloud.starter.web.context;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * -- 分页实体
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "PageResult", description = "分页实体")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Long pageIndex;

    /**
     * 每页显示页数
     */
    @Schema(description = "每页显示页数")
    private Long pageSize;

    /**
     * 总条数
     */
    @Schema(description = "总条数")
    private Long total;

    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private Long pages;

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> records;

    public List<T> getRecords() {
        if(Objects.isNull(records)) {
            return Collections.emptyList();
        }
        return records;
    }

    /**
     * 是否首页
     *
     * @return boolean
     */
    @Schema(description = "是否首页")
    public boolean isFirst() {
        return 1 == pageIndex;
    }

    /**
     * 是否尾页
     *
     * @return boolean
     */
    @Schema(description = "是否尾页")
    public boolean isLast() {
        return pageIndex.equals(pages);
    }
}

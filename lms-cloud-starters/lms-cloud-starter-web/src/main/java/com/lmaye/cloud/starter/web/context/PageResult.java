package com.lmaye.cloud.starter.web.context;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
@Accessors(chain = true)
@ApiModel(value = "PageResult", description = "分页实体")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Long pageIndex;

    /**
     * 每页显示页数
     */
    @ApiModelProperty("每页显示页数")
    private Long pageSize;

    /**
     * 总条数
     */
    @ApiModelProperty("总条数")
    private Long total;

    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Long pages;

    /**
     * 数据列表
     */
    @ApiModelProperty("数据列表")
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
    @ApiModelProperty("是否首页")
    public boolean isFirst() {
        return 1 == pageIndex;
    }

    /**
     * 是否尾页
     *
     * @return boolean
     */
    @ApiModelProperty("是否尾页")
    public boolean isLast() {
        return pageIndex.equals(pages);
    }
}

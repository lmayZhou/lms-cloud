package com.lmaye.cloud.starter.web.query;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "ListQuery", description = "列表查询参数")
public class ListQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 条件
     */
    @Valid
    @Schema(description = "条件")
    private Query query;

    /**
     * 排序
     */
    @Valid
    @Schema(description = "排序")
    private Sort sort;
}

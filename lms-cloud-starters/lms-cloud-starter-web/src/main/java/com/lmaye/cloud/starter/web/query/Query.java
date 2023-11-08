package com.lmaye.cloud.starter.web.query;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * -- 查询参数
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
@Tag(name = "Query", description = "查询参数")
public class Query implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 等值查询
     */
    @Valid
    @Schema(description = "等值查询")
    private List<TermQuery> terms;

    /**
     * 模糊查询
     */
    @Valid
    @Schema(description = "模糊查询")
    private List<MatchQuery> matches;

    /**
     * 范围查询
     */
    @Valid
    @Schema(description = "范围查询")
    private List<RangeQuery> ranges;

    /**
     * IN查询
     */
    @Valid
    @Schema(description = "IN查询")
    private List<InQuery> ins;

    /**
     * 且查询
     */
    @Valid
    @Schema(description = "且查询")
    private Query must;

    /**
     * 或查询
     */
    @Valid
    @Schema(description = "或查询")
    private Query should;

    /**
     * 是否为空
     *
     * @return boolean
     */
    @Schema(hidden = true)
    public boolean isNull() {
        return CollectionUtils.isEmpty(terms) && CollectionUtils.isEmpty(matches)
                && CollectionUtils.isEmpty(ranges) && CollectionUtils.isEmpty(ins);
    }
}

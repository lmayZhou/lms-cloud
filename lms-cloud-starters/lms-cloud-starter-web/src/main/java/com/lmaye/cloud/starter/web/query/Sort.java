package com.lmaye.cloud.starter.web.query;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "Sort", description = "排序参数")
public class Sort implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 顺序
     */
    @Schema(description = "顺序")
    @NotEmpty
    private List<Order> order;
}

package com.example.swagger.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- Test
 *
 * @author Lmay Zhou
 * @date 2023/11/7 23:45
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "TestVO", description = "Test请求参数")
public class TestVO implements Serializable {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;
}

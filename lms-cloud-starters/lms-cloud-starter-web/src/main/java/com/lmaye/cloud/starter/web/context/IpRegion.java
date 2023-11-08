package com.lmaye.cloud.starter.web.context;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- IpRegion
 *
 * @author Lmay Zhou
 * @date 2023/10/21 01:03
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Tag(name = "IpRegion", description = "IpRegion")
public class IpRegion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 国家
     */
    @Schema(description = "国家")
    private String country;

    /**
     * 地区
     */
    @Schema(description = "地区")
    private String region;

    /**
     * 省
     */
    @Schema(description = "省")
    private String province;

    /**
     * 市
     */
    @Schema(description = "市")
    private String city;

    /**
     * 运营商
     */
    @Schema(description = "运营商")
    private String isp;
}

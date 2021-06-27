package com.lmaye.cloud.example.elasticsearch.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * -- ServiceLog Entity
 * - 业务日志
 *
 * @author lmay.Zhou
 * @date 2020/12/3 13:50
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ServiceLogEntity", description = "业务日志")
@Document(indexName = "service_logs", indexStoreType = "_doc", useServerConfiguration = true)
public class ServiceLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "编号", required = true)
    private Long id;

    /**
     * 应用ID
     */
    @NotBlank
    @ApiModelProperty(value = "应用ID", required = true)
    private String appId;

    /**
     * 模块ID
     */
    @NotBlank
    @ApiModelProperty(value = "模块ID", required = true)
    private String moduleId;

    /**
     * 用户名
     */
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    /**
     * 标题
     */
    @NotBlank
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 操作时间
     */
    @NotNull
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime operateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 滚动ID(深度分页查询)
     */
    @ApiModelProperty("滚动ID(深度分页查询)")
    private String scrollId;
}

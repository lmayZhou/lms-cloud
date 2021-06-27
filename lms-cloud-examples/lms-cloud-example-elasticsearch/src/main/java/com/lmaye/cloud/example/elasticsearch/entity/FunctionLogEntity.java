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
 * -- FunctionLog Entity
 * - 功能日志
 *
 * @author lmay.Zhou
 * @date 2020/12/3 13:52
 * @email lmay@lmaye.com
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "FunctionLogEntity", description = "功能日志")
@Document(indexName = "function_logs", indexStoreType = "_doc", useServerConfiguration = true)
public class FunctionLogEntity implements Serializable {
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
     * 方法名称
     */
    @NotBlank
    @ApiModelProperty(value = "方法名称", required = true)
    private String functionName;

    /**
     * 用户名
     */
    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    /**
     * IP地址
     */
    @NotBlank
    @ApiModelProperty(value = "IP地址", required = true)
    private String ip;

    /**
     * 操作时间
     */
    @NotNull
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "操作时间", required = true)
    private LocalDateTime operateTime;

    /**
     * 消耗时间(ms)
     */
    @NotNull
    @ApiModelProperty(value = "消耗时间(ms)", required = true)
    private Long consumeTime;

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

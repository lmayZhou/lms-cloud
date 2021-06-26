package com.lmaye.cloud.examples;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * --
 *
 * @author lmay.Zhou
 * @date 2020/12/1 9:39
 * @email lmay@lmaye.com
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "TestBean", description = "测试类")
public class TestBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名字", required = true)
    private String name;

    @ApiModelProperty("性别")
    private Integer sex;
}

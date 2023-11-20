package com.lmaye.cloud.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * -- FullEntity
 * - 完整的实体
 *
 * @author Lmay Zhou
 * @date 2023/9/7 22:20
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FullEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    @Version
    @TableField(value = "version", fill = FieldFill.INSERT)
    private Long version;

    /**
     * 修改人
     */
    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private Long lastModifiedBy;

    /**
     * 修改时间
     */
    @TableField(value = "last_modified_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedAt;
}

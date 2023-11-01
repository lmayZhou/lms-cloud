package com.lmaye.cloud.starter.web.context;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * -- User Token
 * - 增强Token
 *
 * @author lmay.Zhou
 * @date 2020/12/22 18:51
 * @email lmay@lmaye.com
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserToken", description = "用户Token")
public class UserToken extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;

    public UserToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserToken(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }
}

package com.lmaye.cloud.starter.web.context;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "UserToken", description = "用户Token")
public class UserToken extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    private Long id;

    public UserToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserToken(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, authorities);
        this.id = id;
    }
}

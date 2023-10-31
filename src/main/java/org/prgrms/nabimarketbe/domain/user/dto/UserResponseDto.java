package org.prgrms.nabimarketbe.domain.user.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private final Long userId;

    private final String nickName;

    private List<String> roles;

    private Collection<? extends GrantedAuthority> authorities;

    private final LocalDateTime modifiedDate;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickname();
        this.roles = user.getRoles();
        this.authorities = user.getAuthorities();
        this.modifiedDate = user.getModifiedDate();
    }

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
            .userId(user.getUserId())
            .nickName(user.getNickname())
            .roles(user.getRoles())
            .authorities(user.getAuthorities())
            .modifiedDate(user.getModifiedDate())
            .build();
    }
}

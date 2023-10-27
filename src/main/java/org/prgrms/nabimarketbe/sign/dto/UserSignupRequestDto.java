package org.prgrms.nabimarketbe.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestDto {
    private String password;
    private String nickname;
    private String provider;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
//                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
//                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public User toEntity() {
        return User.builder()
//                .email(email)
                .nickname(nickname)
//                .name(name)
                .provider(provider)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}

package org.prgrms.nabimarketbe.sign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequestDto {
    private String nickname;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .nickname(nickname)
//                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
    }
}

package org.prgrms.nabimarketbe.user;

import lombok.Builder;
import lombok.Getter;

import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;
import org.prgrms.nabimarketbe.BaseDomain;

@Getter
public class User extends BaseDomain {
    private Long userId;

    private String accountId;

    private String nickname;

    private String imageUrl;

    private String provider;

    private String role;

    @Builder
    private User(
        String accountId,
        String nickname,
        String imageUrl,
        String provider,
        String role
    ) {
        if (accountId.isBlank() || nickname.isBlank() || provider.isBlank() || role.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.accountId = accountId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.role = role;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

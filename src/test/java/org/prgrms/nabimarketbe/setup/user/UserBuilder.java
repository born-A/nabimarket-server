package org.prgrms.nabimarketbe.setup.user;

import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.setup.oauth2.request.SocialUserInfoDTOBuilder;

public class UserBuilder {
    public static User build() {
        return SocialUserInfoDTOBuilder.build()
            .toEntity("testNickname");
    }
}

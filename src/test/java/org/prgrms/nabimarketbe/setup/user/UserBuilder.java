package org.prgrms.nabimarketbe.setup.user;

import org.prgrms.nabimarketbe.domain.user.Role;
import org.prgrms.nabimarketbe.domain.user.entity.User;

public class UserBuilder {
    public static User createTestEntity() {
        return User.builder()
            .accountId("testAccountId")
            .nickname("testNickname")
            .role(Role.USER.getKey())
            .imageUrl("testImageUrl")
            .provider("KAKAO")
            .build();
    }
}

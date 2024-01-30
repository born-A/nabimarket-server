package setup.user;

import org.prgrms.nabimarketbe.user.entity.Role;
import org.prgrms.nabimarketbe.user.entity.User;

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

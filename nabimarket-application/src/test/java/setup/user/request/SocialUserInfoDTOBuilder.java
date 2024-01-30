package setup.user.request;

import org.prgrms.nabimarketbe.user.dto.request.SocialUserInfoDTO;

public class SocialUserInfoDTOBuilder {
    public static SocialUserInfoDTO createTestDTO() {
        return SocialUserInfoDTO.builder()
            .accountId("testAccount")
            .provider("KAKAO")
            .build();
    }
}

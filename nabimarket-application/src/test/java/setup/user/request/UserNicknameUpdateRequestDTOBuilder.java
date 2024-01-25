package setup.user.request;

import org.prgrms.nabimarketbe.user.dto.request.UserNicknameUpdateRequestDTO;

public class UserNicknameUpdateRequestDTOBuilder {
    public static UserNicknameUpdateRequestDTO createTestDTO() {
        return new UserNicknameUpdateRequestDTO("testUpdateNickname");
    }
}

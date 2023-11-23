package org.prgrms.nabimarketbe.setup.user.request;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserNicknameUpdateRequestDTO;

public class UserNicknameUpdateRequestDTOBuilder {
    public static UserNicknameUpdateRequestDTO build() {
        return new UserNicknameUpdateRequestDTO("testUpdateNickname");
    }
}

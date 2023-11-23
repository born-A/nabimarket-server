package org.prgrms.nabimarketbe.setup.user.request;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserProfileUpdateRequestDTO;

public class UserProfileUpdateRequestDTOBuilder {
    public static UserProfileUpdateRequestDTO build() {
        return new UserProfileUpdateRequestDTO("testImageUrl");
    }
}

package org.prgrms.nabimarketbe.setup.user.request;

import org.prgrms.nabimarketbe.domain.user.dto.request.UserProfileUpdateRequestDTO;

public class UserProfileUpdateRequestDTOBuilder {
    public static UserProfileUpdateRequestDTO createTestDTO() {
        return new UserProfileUpdateRequestDTO("testImageUrl");
    }
}

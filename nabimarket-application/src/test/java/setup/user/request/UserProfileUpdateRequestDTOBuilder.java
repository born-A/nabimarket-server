package setup.user.request;

import org.prgrms.nabimarketbe.user.dto.request.UserProfileUpdateRequestDTO;

public class UserProfileUpdateRequestDTOBuilder {
    public static UserProfileUpdateRequestDTO createTestDTO() {
        return new UserProfileUpdateRequestDTO("testImageUrl");
    }
}

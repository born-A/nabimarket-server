package org.prgrms.nabimarketbe.card.projection;

import org.prgrms.nabimarketbe.user.projection.UserIdResponseDTO;

public record CardUserSummaryResponseDTO(
    CardCondenseResponseDTO cardInfo,
    UserIdResponseDTO userInfo
) {
}

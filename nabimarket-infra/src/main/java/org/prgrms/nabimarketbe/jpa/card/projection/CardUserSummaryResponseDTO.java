package org.prgrms.nabimarketbe.jpa.card.projection;

import org.prgrms.nabimarketbe.jpa.user.projection.UserIdResponseDTO;

public record CardUserSummaryResponseDTO(
    CardCondenseResponseDTO cardInfo,
    UserIdResponseDTO userInfo
) {
}

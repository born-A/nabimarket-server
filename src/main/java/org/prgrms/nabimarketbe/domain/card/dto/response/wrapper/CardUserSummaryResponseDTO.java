package org.prgrms.nabimarketbe.domain.card.dto.response.wrapper;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardCondenseResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserIdResponseDTO;

public record CardUserSummaryResponseDTO(
    CardCondenseResponseDTO cardInfo,
    UserIdResponseDTO userInfo
) {
}

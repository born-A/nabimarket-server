package org.prgrms.nabimarketbe.card.dto.response.wrapper;

import org.prgrms.nabimarketbe.card.dto.response.CardDetailResponseDTO;
import org.prgrms.nabimarketbe.user.dto.response.UserSummaryResponseDTO;

import lombok.Builder;

@Builder
public record CardUserResponseDTO(
    CardDetailResponseDTO cardInfo,
    UserSummaryResponseDTO userInfo
) {
    public static CardUserResponseDTO of(
        CardDetailResponseDTO cardInfo,
        UserSummaryResponseDTO userInfo
    ) {
        return CardUserResponseDTO.builder()
            .cardInfo(cardInfo)
            .userInfo(userInfo)
            .build();
    }
}

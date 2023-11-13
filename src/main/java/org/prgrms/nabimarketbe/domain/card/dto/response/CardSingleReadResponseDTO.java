package org.prgrms.nabimarketbe.domain.card.dto.response;

import lombok.Builder;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserResponseDTO;

@Builder
public record CardSingleReadResponseDTO (
    CardResponseDTO<CardDetail> cardInfo,
    UserResponseDTO<UserSummaryResponseDTO> userInfo
) {
    public static CardSingleReadResponseDTO of(
        CardResponseDTO<CardDetail> cardInfo,
        UserResponseDTO<UserSummaryResponseDTO> userInfo
    ){
        return CardSingleReadResponseDTO.builder()
            .cardInfo(cardInfo)
            .userInfo(userInfo)
            .build();
    }
}

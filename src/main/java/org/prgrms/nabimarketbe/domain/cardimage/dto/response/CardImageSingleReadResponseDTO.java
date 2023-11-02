package org.prgrms.nabimarketbe.domain.cardimage.dto.response;

import lombok.Builder;

@Builder
public record CardImageSingleReadResponseDTO(
        Integer _id,
        String url
) {
    public static CardImageSingleReadResponseDTO of(
            Integer _id,
            String url
    ) {
        return CardImageSingleReadResponseDTO.builder()
                ._id(_id)
                .url(url)
                .build();
    }
}

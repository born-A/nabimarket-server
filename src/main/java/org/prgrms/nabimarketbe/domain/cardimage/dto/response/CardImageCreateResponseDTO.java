package org.prgrms.nabimarketbe.domain.cardimage.dto.response;

import lombok.Builder;

@Builder
public record CardImageCreateResponseDTO(
    Integer _id,
    String url
) {
    public static CardImageCreateResponseDTO of(
            Integer _id,
            String url
    ) {
        return CardImageCreateResponseDTO.builder()
                ._id(_id)
                .url(url)
                .build();
    }
}

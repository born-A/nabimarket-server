package org.prgrms.nabimarketbe.domain.cardimage.dto.response;


public record CardImageSingleReadResponseDTO(
        Integer _id,
        String url
) {
    public static CardImageSingleReadResponseDTO of(
            Integer _id,
            String url
    ) {
        return new CardImageSingleReadResponseDTO(_id, url);
    }
}

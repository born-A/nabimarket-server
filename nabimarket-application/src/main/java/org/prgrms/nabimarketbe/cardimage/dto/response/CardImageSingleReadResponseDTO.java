package org.prgrms.nabimarketbe.cardimage.dto.response;

public record CardImageSingleReadResponseDTO(String url) {
    public static CardImageSingleReadResponseDTO from(String url) {
        return new CardImageSingleReadResponseDTO(url);
    }
}

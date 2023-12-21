package org.prgrms.nabimarketbe.cardImage.dto.response;

public record CardImageUpdateResponseDTO(String url) {
    public static CardImageUpdateResponseDTO from(String url) {
        return new CardImageUpdateResponseDTO(url);
    }
}

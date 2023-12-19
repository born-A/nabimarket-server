package org.prgrms.nabimarketbe.domain.cardimage.dto.response;

public record CardImageUpdateResponseDTO(String url) {
    public static CardImageUpdateResponseDTO from(String url) {
        return new CardImageUpdateResponseDTO(url);
    }
}

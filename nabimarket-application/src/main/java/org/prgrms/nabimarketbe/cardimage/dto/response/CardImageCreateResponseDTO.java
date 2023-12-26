package org.prgrms.nabimarketbe.cardimage.dto.response;

public record CardImageCreateResponseDTO(String url) {
    public static CardImageCreateResponseDTO from(String url) {
        return new CardImageCreateResponseDTO(url);
    }
}

package org.prgrms.nabimarketbe.domain.cardimage.dto.response;


public record CardImageCreateResponseDTO(String url) {
    public static CardImageCreateResponseDTO from(String url) {
        return new CardImageCreateResponseDTO(url);
    }
}

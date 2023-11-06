package org.prgrms.nabimarketbe.domain.cardimage.dto.response;


public record CardImageCreateResponseDTO(   // TODO: _id 지우기
    Integer _id,
    String url
) {
    public static CardImageCreateResponseDTO of(
            Integer _id,
            String url
    ) {
        return new CardImageCreateResponseDTO(_id, url);
    }
}

package org.prgrms.nabimarketbe.domain.cardimage.dto.request;

import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;

public record CardImageCreateRequestDTO(
        Integer _id,
        String binary
) {
}

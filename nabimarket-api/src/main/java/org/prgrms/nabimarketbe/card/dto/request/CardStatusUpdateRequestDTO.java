package org.prgrms.nabimarketbe.card.dto.request;

import org.prgrms.nabimarketbe.annotation.ValidEnum;
import org.prgrms.nabimarketbe.card.entity.CardStatus;

public record CardStatusUpdateRequestDTO(
    @ValidEnum(enumClass = CardStatus.class, message = "유효하지 않은 상태입니다.")
    CardStatus status
) {
}

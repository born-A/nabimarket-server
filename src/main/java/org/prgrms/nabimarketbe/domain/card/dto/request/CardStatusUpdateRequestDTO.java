package org.prgrms.nabimarketbe.domain.card.dto.request;

import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.global.annotation.ValidEnum;

public record CardStatusUpdateRequestDTO(
    @ValidEnum(enumClass = CardStatus.class, message = "유효하지 않은 상태입니다.")
    CardStatus status
) {
}

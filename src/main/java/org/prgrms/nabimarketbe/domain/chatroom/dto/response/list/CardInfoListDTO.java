package org.prgrms.nabimarketbe.domain.chatroom.dto.response.list;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
public class CardInfoListDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}

package org.prgrms.nabimarketbe.domain.chatroom.dto.response.list;

import lombok.Getter;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

@Getter
public class CardInfoListDTO {
    Long cardId;
    String itemName;
    PriceRange priceRange;
    String thumbnail;
}

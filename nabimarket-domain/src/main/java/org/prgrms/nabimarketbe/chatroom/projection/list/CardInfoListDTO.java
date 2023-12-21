package org.prgrms.nabimarketbe.chatroom.projection.list;

import org.prgrms.nabimarketbe.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfoListDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}

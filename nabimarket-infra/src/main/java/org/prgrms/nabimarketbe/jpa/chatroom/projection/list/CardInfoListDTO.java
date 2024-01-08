package org.prgrms.nabimarketbe.jpa.chatroom.projection.list;

import org.prgrms.nabimarketbe.jpa.item.entity.PriceRange;

import lombok.Getter;

@Getter
public class CardInfoListDTO {
    private Long cardId;

    private String itemName;

    private PriceRange priceRange;

    private String thumbnail;
}

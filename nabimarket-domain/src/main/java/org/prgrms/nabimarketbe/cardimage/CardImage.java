package org.prgrms.nabimarketbe.cardimage;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.nabimarketbe.BasePOJO;
import org.prgrms.nabimarketbe.card.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;

@Getter
public class CardImage extends BasePOJO {
    private Long cardImageId;
    private String imageUrl;
    private Card card;

    @Builder
    public CardImage(
        String imageUrl,
        Card card
    ) {
        if (imageUrl.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        if (card == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.imageUrl = imageUrl;
        this.card = card;
    }
}

package org.prgrms.nabimarketbe.dibs;

import org.prgrms.nabimarketbe.BaseDomain;
import org.prgrms.nabimarketbe.card.Card;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;
import org.prgrms.nabimarketbe.user.User;

import lombok.Getter;

@Getter
public class Dib extends BaseDomain {
    private Long dibId;

    private User user;

    private Card card;

    public Dib(
        User user,
        Card card
    ) {
        if (user == null || card == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.user = user;
        this.card = card;
    }
}

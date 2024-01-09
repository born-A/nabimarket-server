package org.prgrms.nabimarketbe.notification;

import org.prgrms.nabimarketbe.BasePOJO;
import org.prgrms.nabimarketbe.card.Card;
import org.prgrms.nabimarketbe.user.User;

import lombok.Getter;

@Getter
public class Notification extends BasePOJO {
    private Long notificationId;

    private String content;

    private User receiver;

    private Card card;

    private boolean isRead;

    public Notification(
        User user,
        Card card,
        String content
    ) {
        this.receiver = user;
        this.card = card;
        this.content = content;
        this.isRead = false;
    }

    public void updateToRead() {
        this.isRead = true;
    }
}

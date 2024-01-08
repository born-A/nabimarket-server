package org.prgrms.nabimarketbe.event;

import org.prgrms.nabimarketbe.jpa.card.entity.Card;
import org.prgrms.nabimarketbe.jpa.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationCreateEvent {
    private final User receiver;

    private final Card card;

    private final String content;
}

package org.prgrms.nabimarketbe.global.event;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationCreateEvent {
    private final User receiver;

    private final Card card;

    private final String content;
}

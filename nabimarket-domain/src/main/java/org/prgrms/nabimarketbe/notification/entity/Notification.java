package org.prgrms.nabimarketbe.notification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.prgrms.nabimarketbe.BaseEntity;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifiaction_id", nullable = false)
    private Long notificationId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(name = "is_read", nullable = false)
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

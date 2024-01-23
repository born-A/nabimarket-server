package org.prgrms.nabimarketbe.card.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.prgrms.nabimarketbe.BaseEntity;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "card_title", nullable = false, length = 30)
    private String cardTitle;

    @Column(name = "thumbnail")
    private String thumbnail;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Lob
    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "trade_area", nullable = false, length = 30)
    private String tradeArea;

    @NotNull(message = "비울 수 없는 값입니다.")
    @Column(name = "poke_available", nullable = false)
    private Boolean pokeAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    private TradeType tradeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Min(value = 0, message = "최소 0 이상의 값이 필요합니다.")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Min(value = 0, message = "최소 0 이상의 값이 필요합니다.")
    @Column(name = "dib_count", nullable = false)
    private Integer dibCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Card(
        String cardTitle,
        String thumbnail,
        String content,
        String tradeArea,
        Boolean pokeAvailable,
        TradeType tradeType,
        Item item,
        User user
    ) {
        if (cardTitle.isBlank() || content.isBlank() || thumbnail.isBlank()) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        if (pokeAvailable == null || tradeType == null || tradeArea == null || item == null || user == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.cardTitle = cardTitle;
        this.thumbnail = thumbnail;
        this.content = content;
        this.tradeArea = tradeArea;
        this.pokeAvailable = pokeAvailable;
        this.tradeType = tradeType;
        this.status = CardStatus.TRADE_AVAILABLE;
        this.viewCount = 0;
        this.dibCount = 0;
        this.isActive = true;
        this.item = item;
        this.user = user;
    }

    public void updateThumbnail(String url) {
        this.thumbnail = url;
    }

    public void updateViewCount() {
        this.viewCount += 1;
    }

    // TODO : dibCount 동시성
    public void increaseDibCount() {
        this.dibCount += 1;
    }

    public void decreaseDibCount() {
        this.dibCount -= 1;
    }

    public void updateCardStatusToTradeAvailable() {
        this.status = CardStatus.TRADE_AVAILABLE;
    }

    public void updateCardStatusToReserved() {
        this.status = CardStatus.RESERVED;
    }

    public void updateCardStatusToTradeComplete() {
        this.status = CardStatus.TRADE_COMPLETE;
    }

    public void deleteCard() {
        this.isActive = false;
    }

    public Boolean isPokeAvailable() {
        return pokeAvailable;
    }

    public void updateCard(
        String cardTitle,
        String thumbnail,
        Boolean pokeAvailable,
        String content,
        TradeType tradeType,
        String tradeArea
    ) {
        this.cardTitle = cardTitle;
        this.thumbnail = thumbnail;
        this.pokeAvailable = pokeAvailable;
        this.content = content;
        this.tradeType = tradeType;
        this.tradeArea = tradeArea;
    }
}

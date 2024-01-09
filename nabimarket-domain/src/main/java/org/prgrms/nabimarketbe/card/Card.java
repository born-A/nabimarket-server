package org.prgrms.nabimarketbe.card;

import lombok.Builder;
import lombok.Getter;
import org.prgrms.nabimarketbe.BaseDomain;
import org.prgrms.nabimarketbe.error.BaseException;
import org.prgrms.nabimarketbe.error.ErrorCode;
import org.prgrms.nabimarketbe.item.Item;
import org.prgrms.nabimarketbe.user.User;

@Getter
public class Card extends BaseDomain {
    private Long cardId;
    private String cardTitle;
    private String thumbnail;
    private String content;
    private String tradeArea;
    private Boolean pokeAvailable;
    private TradeType tradeType;
    private CardStatus status;
    private Integer viewCount;
    private Integer dibCount;
    private Boolean isActive;
    private Item item;
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

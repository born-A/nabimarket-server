package org.prgrms.nabimarketbe.domain.card.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.item.entity.Item;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @NotNull @NotBlank
    private String cardTitle;

    private String thumbNailImage;

    @Lob
    private String content;

    private String tradeArea;

    private Boolean poke;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    private Integer viewCount;

    private Integer dibCount;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public Card(
            String cardTitle,
            String thumbNailImage,
            String content,
            String tradeArea,
            Boolean poke,
            TradeType tradeType,
            Item item
    ) {
        this.cardTitle = cardTitle;
        this.thumbNailImage = thumbNailImage;
        this.content = content;
        this.tradeArea = tradeArea;
        this.poke = poke;
        this.tradeType = tradeType;
        this.status = CardStatus.TRADE_AVAILABLE;
        this.viewCount = 0;
        this.dibCount = 0;
        this.item = item;
    }
}

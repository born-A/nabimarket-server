package org.prgrms.nabimarketbe.domain.card.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
import org.prgrms.nabimarketbe.global.annotation.ValidEnum;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "card_title", nullable = false)
    private String cardTitle;

//    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "thumbnail_image")
    private String thumbNailImage;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "trade_area", nullable = false)
    private String tradeArea;

    @NotNull(message = "비울 수 없는 값입니다.")
    @Column(name = "poke", nullable = false)
    private Boolean poke;

//    @ValidEnum(enumClass = TradeType.class, message = "유효하지 않은 거래 방식입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    private TradeType tradeType;

//    @ValidEnum(enumClass = CardStatus.class, message = "유효하지 않은 카드 상태입니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Min(value = 0, message = "최소 0 이상의 값이 필요합니다.")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Min(value = 0, message = "최소 0 이상의 값이 필요합니다.")
    @Column(name = "dib_count", nullable = false)
    private Integer dibCount;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    private Card(
            String cardTitle,
            String content,
            String tradeArea,
            Boolean poke,
            TradeType tradeType,
            Item item
    ) {
        if (cardTitle.isBlank() || content.isBlank() || tradeArea.isBlank()) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        if (poke == null || tradeType == null || item == null) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        this.cardTitle = cardTitle;
        this.content = content;
        this.tradeArea = tradeArea;
        this.poke = poke;
        this.tradeType = tradeType;
        this.status = CardStatus.TRADE_AVAILABLE;
        this.viewCount = 0;
        this.dibCount = 0;
        this.item = item;
    }

    public void updateThumbNailImage(String url) {
        thumbNailImage = url;
    }
}

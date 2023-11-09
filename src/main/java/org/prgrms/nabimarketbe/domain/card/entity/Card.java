package org.prgrms.nabimarketbe.domain.card.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.item.entity.Item;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Card(
            String cardTitle,
            String thumbNailImage,
            String content,
            String tradeArea,
            Boolean poke,
            TradeType tradeType,
            Item item,
            User user
    ) {
        if (cardTitle.isBlank() || content.isBlank() || tradeArea.isBlank()) {  // TODO: 구조 변경 후 thumbNailImage null 처리 추가
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        if (poke == null || tradeType == null || item == null || user == null) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

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
        this.user = user;
    }

    public void updateThumbNailImage(String url) {
        this.thumbNailImage = url;
    }
}

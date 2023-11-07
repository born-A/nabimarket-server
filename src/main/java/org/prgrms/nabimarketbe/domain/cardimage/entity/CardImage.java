package org.prgrms.nabimarketbe.domain.cardimage.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "card_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long cardImageId;

    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Builder
    public CardImage(
            String imageUrl,
            Card card
    ) {
        if (imageUrl.isBlank()) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        if (card == null) {
            throw new BaseException(ErrorCode.UNKNOWN);
        }

        this.imageUrl = imageUrl;
        this.card = card;
    }
}

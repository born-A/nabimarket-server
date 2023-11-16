package org.prgrms.nabimarketbe.domain.cardimage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_image_id", nullable = false)
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
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        if (card == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        this.imageUrl = imageUrl;
        this.card = card;
    }
}

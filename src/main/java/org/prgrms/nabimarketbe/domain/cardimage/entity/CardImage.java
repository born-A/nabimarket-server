package org.prgrms.nabimarketbe.domain.cardimage.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.prgrms.nabimarketbe.global.BaseEntity;
import org.prgrms.nabimarketbe.domain.card.entity.Card;

import javax.persistence.*;

@Entity
@Table(name = "card_images")
@NoArgsConstructor
@Getter
public class CardImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardImageId;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Builder
    public CardImage(
            String imageUrl,
            Card card
    ) {
        this.imageUrl = imageUrl;
        this.card = card;
    }
}

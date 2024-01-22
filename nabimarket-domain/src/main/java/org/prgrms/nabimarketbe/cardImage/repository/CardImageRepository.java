package org.prgrms.nabimarketbe.cardImage.repository;

import java.util.List;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.cardImage.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImageRepository extends JpaRepository<CardImage, Long> {
    List<CardImage> findAllByCard(Card card);

    void deleteAllByCard(Card card);
}

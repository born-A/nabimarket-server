package org.prgrms.nabimarketbe.domain.cardimage.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.cardimage.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardImageRepository extends JpaRepository<CardImage, Long> {
    List<CardImage> findAllByCard(Card card);
}

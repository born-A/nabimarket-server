package org.prgrms.nabimarketbe.domain.card.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
    Optional<Card> findByCardIdAndUser(Long cardId, User user);
}

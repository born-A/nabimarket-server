package org.prgrms.nabimarketbe.jpa.dibs.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.jpa.card.entity.Card;
import org.prgrms.nabimarketbe.jpa.dibs.entity.Dib;
import org.prgrms.nabimarketbe.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DibRepository extends JpaRepository<Dib, Long>, DibRepositoryCustom {
    Optional<Dib> findDibByUserAndCard(User user, Card card);

    boolean existsDibByCardAndUser(Card card, User user);
}

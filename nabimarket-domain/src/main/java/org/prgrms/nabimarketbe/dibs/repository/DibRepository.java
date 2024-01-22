package org.prgrms.nabimarketbe.dibs.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.dibs.entity.Dib;
import org.prgrms.nabimarketbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DibRepository extends JpaRepository<Dib, Long>, DibRepositoryCustom {
    Optional<Dib> findDibByUserAndCard(User user, Card card);

    boolean existsDibByCardAndUser(Card card, User user);
}

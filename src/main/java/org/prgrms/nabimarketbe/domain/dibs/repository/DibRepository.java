package org.prgrms.nabimarketbe.domain.dibs.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.dibs.entity.Dib;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DibRepository extends JpaRepository<Dib, Long> {
	Optional<Dib> findDibByUserAndCard(User user, Card card);
}

package org.prgrms.nabimarketbe.domain.completeRequest.repository;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompleteRequestRepository extends JpaRepository<CompleteRequest, Long>,CompleteRequestRepositryCustom {
    Optional<CompleteRequest> findCompleteRequestByFromCardAndToCard(Card fromCard, Card toCard);
}

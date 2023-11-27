package org.prgrms.nabimarketbe.domain.completeRequest.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequest;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompleteRequestRepository extends JpaRepository<CompleteRequest, Long>,
    CompleteRequestRepositryCustom {
    @Query("select c "
        + "from CompleteRequest c "
        + "where c.fromCard = :fromCard and c.toCard = :toCard "
        + "or c.fromCard = :toCard and c.toCard = :fromCard")
    Optional<CompleteRequest> findCompleteRequestByFromCardAndToCard(
        @Param("fromCard") Card fromCard,
        @Param("toCard") Card toCard
    );

    @Query(
        "select count(c.completeRequestId) > 0 "
            + "from CompleteRequest c "
            + "where c.fromCard = :fromCard and c.toCard = :toCard "
            + "or c.fromCard = :toCard and c.toCard = :fromCard")
    Boolean exists(@Param("fromCard") Card fromCard, @Param("toCard") Card toCard);
}

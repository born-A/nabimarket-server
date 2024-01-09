package org.prgrms.nabimarketbe.jpa.completerequest.repository;

import java.util.Optional;

import org.prgrms.nabimarketbe.jpa.card.entity.Card;
import org.prgrms.nabimarketbe.jpa.completerequest.entity.CompleteRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompleteRequestRepository extends JpaRepository<CompleteRequest, Long>,
    CompleteRequestRepositryCustom {
    @Query("select c "
        + "from CompleteRequest c "
        + "where c.fromCard = :fromCard and c.toCard = :toCard and c.completeRequestStatus != 'REFUSED' " +
        "and c.fromCard = :fromCard and c.toCard = :toCard and c.completeRequestStatus != 'DELETED' "
        + "or c.fromCard = :toCard and c.toCard = :fromCard and c.completeRequestStatus != 'REFUSED'" +
        "and c.fromCard = :toCard and c.toCard = :fromCard and c.completeRequestStatus != 'DELETED'")
    Optional<CompleteRequest> findCompleteRequestByFromCardAndToCard(
        @Param("fromCard") Card fromCard,
        @Param("toCard") Card toCard
    );

    @Query(
        "select count(c.completeRequestId) > 0 "
            + "from CompleteRequest c "
            + "where c.fromCard = :fromCard and c.toCard = :toCard and c.completeRequestStatus != 'REFUSED' " +
            "and c.fromCard = :fromCard and c.toCard = :toCard and c.completeRequestStatus != 'DELETED' "
            + "or c.fromCard = :toCard and c.toCard = :fromCard and c.completeRequestStatus != 'REFUSED' "
            + "and c.fromCard = :toCard and c.toCard = :fromCard and c.completeRequestStatus != 'DELETED'")
    Boolean exists(@Param("fromCard") Card fromCard, @Param("toCard") Card toCard);

    @Query(
        "select count(c.completeRequestId) > 0 "
            + "from CompleteRequest c "
            + "where c.fromCard = :fromCard and c.completeRequestStatus != 'REFUSED' " +
            "and c.fromCard = :fromCard and c.completeRequestStatus != 'DELETED' "
            + "or c.toCard = :fromCard and c.completeRequestStatus != 'REFUSED' " +
            "and  c.toCard = :fromCard and c.completeRequestStatus != 'DELETED'")
    Boolean existsByFromCard(@Param("fromCard") Card fromCard);

    @Query(
        "select count(c.completeRequestId) > 0 "
            + "from CompleteRequest c "
            + "where c.toCard = :toCard and c.completeRequestStatus != 'REFUSED' " +
            "and c.toCard = :toCard and c.completeRequestStatus != 'DELETED' "
            + "or c.fromCard = :toCard and c.completeRequestStatus != 'REFUSED'" +
            "and c.fromCard = :toCard and c.completeRequestStatus != 'DELETED'")
    Boolean existsByToCard(@Param("toCard") Card toCard);
}

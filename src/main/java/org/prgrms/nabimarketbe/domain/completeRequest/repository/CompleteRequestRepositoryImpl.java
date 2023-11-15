package org.prgrms.nabimarketbe.domain.completeRequest.repository;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.completeRequest.entity.QCompleteRequest.completeRequest;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequestStatus;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CompleteRequestRepositoryImpl implements CompleteRequestRepositryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public HistoryListReadLimitResponseDTO getHistoryBySize(Integer size) {
        List<HistoryListReadResponseDTO> historyList = jpaQueryFactory
            .select(
                Projections.fields(
                    HistoryListReadResponseDTO.class,
                    completeRequest.completeRequestId,
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        completeRequest.fromCard.cardId,
                        completeRequest.fromCard.item.itemName,
                        completeRequest.fromCard.thumbnail.as("thumbnail"),
                        completeRequest.fromCard.item.priceRange
                    ).as("fromCard"),
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        completeRequest.toCard.cardId,
                        completeRequest.toCard.item.itemName,
                        completeRequest.toCard.thumbnail.as("thumbnail"),
                        completeRequest.toCard.item.priceRange
                    ).as("toCard"),
                    completeRequest.createdDate.as("createdAt"),
                    completeRequest.modifiedDate.as("modifiedAt")
                )
            )
            .from(completeRequest)
            .leftJoin(card).on(card.cardId.eq(completeRequest.fromCard.cardId))
            .leftJoin(card).on(card.cardId.eq(completeRequest.toCard.cardId).as("toCardAlias"))
            .where(completeRequest.completeRequestStatus.eq(CompleteRequestStatus.ACCEPTED))
            .orderBy(completeRequest.modifiedDate.desc())
            .limit(size)
            .fetch();

        return new HistoryListReadLimitResponseDTO(historyList);
    }

    @Override
    public HistoryListReadPagingResponseDTO getHistoryByUser(
        User user,
        String cursorId,
        Integer size
    ) {
        List<HistoryListReadResponseDTO> historyList = jpaQueryFactory
            .select(
                Projections.fields(
                    HistoryListReadResponseDTO.class,
                    completeRequest.completeRequestId,
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        completeRequest.fromCard.cardId,
                        completeRequest.fromCard.item.itemName,
                        completeRequest.fromCard.thumbnail.as("thumbnail"),
                        completeRequest.fromCard.item.priceRange
                    ).as("fromCard"),
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        completeRequest.toCard.cardId,
                        completeRequest.toCard.item.itemName,
                        completeRequest.toCard.thumbnail.as("thumbnail"),
                        completeRequest.toCard.item.priceRange
                    ).as("toCard"),
                    completeRequest.createdDate.as("createdAt"),
                    completeRequest.modifiedDate.as("modifiedAt")
                )
            )
            .from(completeRequest)
            .leftJoin(card).on(card.cardId.eq(completeRequest.fromCard.cardId))
            .leftJoin(card).on(card.cardId.eq(completeRequest.toCard.cardId).as("toCardAlias"))
            .where(cursorIdLessThan(cursorId),
                completeRequest.completeRequestStatus.eq(CompleteRequestStatus.ACCEPTED),
                completeRequest.fromCard.user.eq(user).or(completeRequest.toCard.user.eq(user)))
            .orderBy(completeRequest.modifiedDate.desc())
            .limit(size)
            .fetch();

        String nextCursor = historyList.size() < size
            ? null : createCursorId(historyList.get(historyList.size() - 1));

        return new HistoryListReadPagingResponseDTO(historyList,nextCursor);
    }

    /**
     * 커스텀 커서 id가 주어진 cursorId보다 작은지 확인
     */
    private BooleanExpression cursorIdLessThan(String cursorId) {
        if (cursorId == null) {
            return null;
        }

        StringTemplate stringTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            completeRequest.createdDate,
            ConstantImpl.create("%Y%m%d%H%i%s")
        );

        return stringTemplate.concat(StringExpressions.lpad(
            completeRequest.completeRequestId.stringValue(),
            8,
            '0'
        )).lt(cursorId);
    }

    /**
     * 커서 id 생성
     */
    private String createCursorId(HistoryListReadResponseDTO historyListReadResponseDTO) {
        return historyListReadResponseDTO.getCreatedAt().toString()
            .replace("T", "")
            .replace("-", "")
            .replace(":", "")
            + String.format(
                "%08d",
                historyListReadResponseDTO.getFromCard().getCardId()
                    + historyListReadResponseDTO.getToCard().getCardId()
            );
    }
}

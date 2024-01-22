package org.prgrms.nabimarketbe.completeRequest.repository;

import java.util.List;

import org.prgrms.nabimarketbe.card.entity.QCard;
import org.prgrms.nabimarketbe.card.projection.CardSummaryResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.entity.CompleteRequestStatus;
import org.prgrms.nabimarketbe.completeRequest.entity.QCompleteRequest;
import org.prgrms.nabimarketbe.completeRequest.projection.HistoryListReadResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.completeRequest.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.user.entity.User;
import org.prgrms.nabimarketbe.util.QueryDslUtil;
import org.springframework.data.domain.Sort;

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
                    QCompleteRequest.completeRequest.completeRequestId,
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        QCompleteRequest.completeRequest.fromCard.cardId,
                        QCompleteRequest.completeRequest.fromCard.item.itemName,
                        QCompleteRequest.completeRequest.fromCard.thumbnail,
                        QCompleteRequest.completeRequest.fromCard.item.priceRange
                    ).as("fromCard"),
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        QCompleteRequest.completeRequest.toCard.cardId,
                        QCompleteRequest.completeRequest.toCard.item.itemName,
                        QCompleteRequest.completeRequest.toCard.thumbnail,
                        QCompleteRequest.completeRequest.toCard.item.priceRange
                    ).as("toCard"),
                    QCompleteRequest.completeRequest.createdDate.as("createdAt"),
                    QCompleteRequest.completeRequest.modifiedDate.as("modifiedAt")
                )
            )
            .from(QCompleteRequest.completeRequest)
            .leftJoin(QCard.card)
            .on(QCard.card.cardId.eq(QCompleteRequest.completeRequest.fromCard.cardId))
            .leftJoin(QCard.card)
            .on(QCard.card.cardId.eq(QCompleteRequest.completeRequest.toCard.cardId).as("toCardAlias"))
            .where(QCompleteRequest.completeRequest.completeRequestStatus.eq(CompleteRequestStatus.ACCEPTED))
            .orderBy(
                QueryDslUtil.getOrderSpecifier(
                    Sort.by(
                        Sort.Order.desc("modifiedDate"),
                        Sort.Order.desc("completeRequestId")
                    ),
                    QCompleteRequest.completeRequest
                )
            )
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
                    QCompleteRequest.completeRequest.completeRequestId,
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        QCompleteRequest.completeRequest.fromCard.cardId,
                        QCompleteRequest.completeRequest.fromCard.item.itemName,
                        QCompleteRequest.completeRequest.fromCard.thumbnail,
                        QCompleteRequest.completeRequest.fromCard.item.priceRange
                    ).as("fromCard"),
                    Projections.fields(
                        CardSummaryResponseDTO.class,
                        QCompleteRequest.completeRequest.toCard.cardId,
                        QCompleteRequest.completeRequest.toCard.item.itemName,
                        QCompleteRequest.completeRequest.toCard.thumbnail,
                        QCompleteRequest.completeRequest.toCard.item.priceRange
                    ).as("toCard"),
                    QCompleteRequest.completeRequest.createdDate.as("createdAt"),
                    QCompleteRequest.completeRequest.modifiedDate.as("modifiedAt")
                )
            )
            .from(QCompleteRequest.completeRequest)
            .leftJoin(QCard.card)
            .on(QCard.card.cardId.eq(QCompleteRequest.completeRequest.fromCard.cardId))
            .leftJoin(QCard.card)
            .on(QCard.card.cardId.eq(QCompleteRequest.completeRequest.toCard.cardId).as("toCardAlias"))
            .where(cursorIdLessThan(cursorId),
                QCompleteRequest.completeRequest.completeRequestStatus.eq(CompleteRequestStatus.ACCEPTED),
                QCompleteRequest.completeRequest.fromCard.user.eq(user)
                    .or(QCompleteRequest.completeRequest.toCard.user.eq(user)))
            .orderBy(
                QueryDslUtil.getOrderSpecifier(
                    Sort.by(
                        Sort.Order.desc("modifiedDate"),
                        Sort.Order.desc("completeRequestId")
                    ),
                    QCompleteRequest.completeRequest
                )
            )
            .limit(size)
            .fetch();

        String nextCursor = historyList.size() < size
            ? null : createCursorId(historyList.get(historyList.size() - 1));

        return new HistoryListReadPagingResponseDTO(historyList, nextCursor);
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
            QCompleteRequest.completeRequest.createdDate,
            ConstantImpl.create("%Y%m%d%H%i%s")
        );

        return stringTemplate.concat(StringExpressions.lpad(
            QCompleteRequest.completeRequest.completeRequestId.stringValue(),
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

package org.prgrms.nabimarketbe.dibs.repository;

import java.util.List;

import org.prgrms.nabimarketbe.card.entity.QCard;
import org.prgrms.nabimarketbe.dibs.entity.QDib;
import org.prgrms.nabimarketbe.dibs.projection.DibListReadResponseDTO;
import org.prgrms.nabimarketbe.dibs.wrapper.DibListReadPagingResponseDTO;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DibRepositoryImpl implements DibRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public DibListReadPagingResponseDTO getUserDibsByDibId(
        Long userId,
        Long cursorId,
        Integer size
    ) {
        List<DibListReadResponseDTO> dibList = jpaQueryFactory.select(
                Projections.fields(
                    DibListReadResponseDTO.class,
                    QDib.dib.dibId,
                    QCard.card.cardId,
                    QCard.card.cardTitle,
                    QCard.card.item.itemName,
                    QCard.card.item.priceRange,
                    QCard.card.thumbnail,
                    QCard.card.status,
                    QCard.card.createdDate.as("createdAt"),
                    QCard.card.modifiedDate.as("modifiedAt")
                )
            )
            .from(QDib.dib)
            .join(QCard.card).on(QCard.card.cardId.eq(QDib.dib.card.cardId))
            .where(
                dibUserIdEquals(userId),
                greaterThan(cursorId)
            )
            .limit(size)
            .fetch();

        Long nextCursorId = generateCursor(dibList, size);

        return DibListReadPagingResponseDTO.of(dibList, nextCursorId);
    }

    private Long generateCursor(
        List<DibListReadResponseDTO> dibList,
        Integer size
    ) {
        if (dibList.size() == size) {
            return dibList.get(dibList.size() - 1).getDibId();
        }

        return null;
    }

    private BooleanExpression dibUserIdEquals(Long userId) {
        if (userId == null) {
            return null;
        }

        return QDib.dib.user.userId.eq(userId);
    }

    private BooleanExpression greaterThan(Long cursorId) {
        if (cursorId == null) {
            return null;
        }

        return QDib.dib.dibId.gt(cursorId);
    }
}

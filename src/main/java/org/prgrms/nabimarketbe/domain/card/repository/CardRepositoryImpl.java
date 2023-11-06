package org.prgrms.nabimarketbe.domain.card.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import java.util.List;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.item.entity.QItem.item;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CardListReadPagingResponseDTO getCardsByCondition(
            CategoryEnum category,
            PriceRange priceRange,
            List<CardStatus> status,
            String title,
            String cursorId,
            Integer size
    ) {
        List<CardListReadResponseDTO> cardList = jpaQueryFactory.select(
                        Projections.fields(
                                CardListReadResponseDTO.class,
                                card.cardId,
                                card.cardTitle,
                                item.itemName,
                                item.priceRange,
                                card.thumbNailImage.as("thumbNail"),
                                card.status,
                                card.createdDate.as("createdAt"),
                                card.modifiedDate.as("modifiedAt")
                        )
                )
                .from(card)
                .leftJoin(item).on(card.item.itemId.eq(item.itemId))
                .where(
                        cursorId(cursorId),
                        category(category),
                        status(status),
                        priceRange(priceRange),
                        title(title)
                )
                .orderBy(card.createdDate.desc())   // 디폴트는 생성일자 최신순 정렬
                .limit(size)
                .fetch();

        String nextCursor = cardList.size() < size ? null : generateCursor(cardList.get(cardList.size() - 1));

        return new CardListReadPagingResponseDTO(cardList, nextCursor);
    }

    private BooleanExpression cursorId(String cursorId){
        if (cursorId == null) {
            return null;
        }

        StringTemplate stringTemplate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                card.createdDate,   // 디폴트는 생성일자 최신순 정렬
                ConstantImpl.create("%Y%m%d%H%i%s")
        );

        return stringTemplate.concat(StringExpressions.lpad(
                        card.cardId.stringValue(),
                        8,
                        '0'
                ))
                .lt(cursorId);
    }

    private BooleanExpression category(CategoryEnum category) {
        if (category == null) {
            return null;
        }

        return item.category.categoryName.eq(category);
    }

    private BooleanExpression status(List<CardStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return null;
        }

        BooleanExpression[] expressions = statuses.stream()
                .map(card.status::eq)
                .toArray(BooleanExpression[]::new);

        return Expressions.anyOf(expressions);
    }

    private BooleanExpression priceRange(PriceRange priceRange) {
        if (priceRange == null) {
            return null;
        }

        return item.priceRange.eq(priceRange);
    }

    private BooleanExpression title(String title) { // TODO: 검색 단어를 '포함'한 제목 검색 가능
        if (title == null) {
            return null;
        }

        return card.cardTitle.eq(title);
    }

    private String generateCursor(CardListReadResponseDTO cardListReadResponseDTO) {
        return cardListReadResponseDTO.getCreatedAt().toString()    // 디폴트는 생성일자 최신순 정렬
                .replace("T", "")
                .replace("-", "")
                .replace(":", "")
                + String.format("%08d", cardListReadResponseDTO.getCardId());
    }
}

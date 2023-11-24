package org.prgrms.nabimarketbe.domain.card.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardFamousResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfoResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionInfo;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.global.util.CursorPaging;
import org.prgrms.nabimarketbe.global.util.QueryDslUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.item.entity.QItem.item;
import static org.prgrms.nabimarketbe.domain.suggestion.entity.QSuggestion.suggestion;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom, CursorPaging {
    private static final int FAMOUS_CARD_SIZE = 5;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CardPagingResponseDTO getCardsByCondition(
        CategoryEnum category,
        PriceRange priceRange,
        List<CardStatus> status,
        String cardTitle,
        String cursorId,
        Pageable pageable
    ) {
        List<CardListReadResponseDTO> cardList = jpaQueryFactory.select(
                Projections.fields(
                    CardListReadResponseDTO.class,
                    card.cardId,
                    card.cardTitle,
                    item.itemName,
                    item.priceRange,
                    card.thumbnail,
                    card.status,
                    card.createdDate.as("createdAt"),
                    card.modifiedDate.as("modifiedAt")
                )
            )
            .from(card)
            .leftJoin(item).on(card.item.itemId.eq(item.itemId))
            .where(
                cursorId(cursorId),
                categoryEquals(category),
                statusEquals(status),
                priceRangeEquals(priceRange),
                titleEquals(cardTitle)
            )
            .orderBy(QueryDslUtil.getOrderSpecifier(pageable.getSort(), card))
            .limit(pageable.getPageSize())
            .fetch();

        String nextCursor = cardList.size() < pageable.getPageSize() ? null : generateCursor(cardList.get(cardList.size() - 1));

        return new CardPagingResponseDTO(cardList, nextCursor);
    }

    @Override
    public CardPagingResponseDTO getMyCardsByStatus(
        User user,
        CardStatus status,
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
                    card.thumbnail,
                    card.status,
                    card.createdDate.as("createdAt"),
                    card.modifiedDate.as("modifiedAt")
                )
            )
            .from(card)
            .leftJoin(item).on(card.item.itemId.eq(item.itemId))
            .where(
                cursorId(cursorId),
                card.status.eq(status),
                card.user.eq(user)
            )
            .orderBy(
                    QueryDslUtil.getOrderSpecifier(
                            Sort.by(
                                    Sort.Order.desc("createdDate"),
                                    Sort.Order.desc("cardId")
                            ),
                            card
                    )
            )
            .limit(size)
            .fetch();

        String nextCursor = cardList.size() < size ? null : generateCursor(cardList.get(cardList.size() - 1));

        return new CardPagingResponseDTO(cardList, nextCursor);
    }

    @Override
    public List<CardSuggestionResponseDTO> getSuggestionAvailableCards(
        Long userId,
        Long targetCardId
    ) {
        List<CardSuggestionResponseDTO> cardList = jpaQueryFactory
            .select(
                Projections.fields(
                    CardSuggestionResponseDTO.class,
                    Projections.fields(
                        CardInfoResponseDTO.class,
                        card.cardId,
                        card.thumbnail,
                        card.cardTitle,
                        card.item.itemName,
                        card.item.priceRange
                    ).as("cardInfo"),
                    Projections.fields(
                        SuggestionInfo.class,
                        suggestion.suggestionType,
                        suggestion.suggestionStatus
                    ).as("suggestionInfo")
                )
            )
            .from(card)
            .leftJoin(suggestion).on(suggestion.fromCard.cardId.eq(card.cardId))
            .where(card.user.userId.eq(userId))
            .distinct()
            .on(suggestion.toCard.cardId.eq(targetCardId))
            .fetch();

        return cardList;
    }

    @Override
    public BooleanExpression cursorId(String cursorId) {
        if (cursorId == null) {
            return null;
        }

        // 생성일자
        StringTemplate dateCursorTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            card.createdDate,
            ConstantImpl.create("%Y%m%d%H%i%s")
        );

        // pk
        StringExpression pkCursorTemplate = StringExpressions.lpad(
                card.cardId.stringValue(),
                8,
                '0'
        );

        return dateCursorTemplate.concat(pkCursorTemplate).lt(cursorId);
    }

    @Override
    public List<CardFamousResponseDTO> getCardsByPopularity() {
        List<CardFamousResponseDTO> cardList = jpaQueryFactory
            .select(
                Projections.fields(
                    CardFamousResponseDTO.class,
                    card.cardId,
                    card.item.itemName,
                    card.item.priceRange,
                    card.thumbnail
                )
            )
            .from(card)
            .where(statusEquals(CardStatus.TRADE_AVAILABLE))
            .orderBy(card.viewCount.desc(), card.dibCount.desc())
            .limit(FAMOUS_CARD_SIZE)
            .fetch();

        return cardList;
    }

    private BooleanExpression statusEquals(CardStatus status) {
        if (status == null) {
            return null;
        }

        return card.status.eq(status);
    }

    private BooleanExpression categoryEquals(CategoryEnum category) {
        if (category == null) {
            return null;
        }

        return item.category.categoryName.eq(category);
    }

    private BooleanExpression statusEquals(List<CardStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return null;
        }

        BooleanExpression[] expressions = statuses.stream()
            .map(card.status::eq)
            .toArray(BooleanExpression[]::new);

        return Expressions.anyOf(expressions);
    }

    private BooleanExpression priceRangeEquals(PriceRange priceRange) {
        if (priceRange == null) {
            return null;
        }

        return item.priceRange.eq(priceRange);
    }

    private BooleanExpression titleEquals(String title) {
        if (title == null) {
            return null;
        }

        return card.cardTitle.contains(title);
    }

    private BooleanExpression isCardActive() {
        return card.isActive.eq(true);
    }

    private String generateCursor(CardListReadResponseDTO cardListReadResponseDTO) {
        return cardListReadResponseDTO.getCreatedAt().toString()    // 디폴트는 생성일자 최신순 정렬
            .replace("T", "")
            .replace("-", "")
            .replace(":", "")
            + String.format("%08d", cardListReadResponseDTO.getCardId());
    }
}

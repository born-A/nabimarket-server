package org.prgrms.nabimarketbe.domain.card.repository;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.item.entity.QItem.item;
import static org.prgrms.nabimarketbe.domain.suggestion.entity.QSuggestion.suggestion;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardInfo;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionInfo;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.domain.user.entity.User;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CardListReadPagingResponseDTO getCardsByCondition(
            CategoryEnum category,
            PriceRange priceRange,
            List<CardStatus> status,
            String cardTitle,
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
                        category(category),
                        status(status),
                        priceRange(priceRange),
                        title(cardTitle)
                )
                .orderBy(card.createdDate.desc())   // 디폴트는 생성일자 최신순 정렬
                .limit(size)
                .fetch();

        String nextCursor = cardList.size() < size ? null : generateCursor(cardList.get(cardList.size() - 1));

        return new CardListReadPagingResponseDTO(cardList, nextCursor);
    }

    @Override
    public CardListReadPagingResponseDTO getMyCardsByStatus(
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
                .orderBy(card.createdDate.desc())   // 디폴트는 생성일자 최신순 정렬
                .limit(size)
                .fetch();

        String nextCursor = cardList.size() < size ? null : generateCursor(cardList.get(cardList.size() - 1));

        return new CardListReadPagingResponseDTO(cardList, nextCursor);
    }

    @Override
    public List<SuggestionAvailableCardResponseDTO> getSuggestionAvailableCards(
        Long userId,
        Long targetCardId
    ) {
        List<SuggestionAvailableCardResponseDTO> cardList = jpaQueryFactory
            .select(
                Projections.fields(
                    SuggestionAvailableCardResponseDTO.class,
                    Projections.fields(
                        CardInfo.class,
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
                .leftJoin(suggestion).on(suggestion.toCard.cardId.eq(targetCardId))
            .where(card.user.userId.eq(userId),
                suggestion.toCard.cardId.eq(targetCardId).or(suggestion.toCard.cardId.isNull()))
            .fetch();

        return cardList;
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

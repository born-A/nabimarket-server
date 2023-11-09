package org.prgrms.nabimarketbe.domain.suggestion.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.entity.QCard;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import java.util.List;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.suggestion.entity.QSuggestion.suggestion;

@RequiredArgsConstructor
public class SuggestionRepositoryImpl implements SuggestionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SuggestionListReadPagingResponseDTO getSuggestionsByType(
            DirectionType directionType,
            SuggestionType suggestionType,
            Long cardId,
            String cursorId,
            Integer size
    ) {
        List<SuggestionListReadResponseDTO> suggestionList1 = jpaQueryFactory
            .select(
                Projections.fields(
                    SuggestionListReadResponseDTO.class,
                    suggestion.suggestionId,
                    getCounter(directionType).cardId,
                    getCounter(directionType).cardTitle,
                    getCounter(directionType).item.itemName,
                    getCounter(directionType).item.priceRange,
                    getCounter(directionType).thumbNailImage.as("thumbnail"),
                    suggestion.suggestionType,
                    suggestion.suggestionStatus,
                    suggestion.createdDate.as("createdAt"),
                    Expressions.as(Expressions.constant(directionType),"directionType")
                )
            )
            .from(suggestion)
            .join(getJoin(directionType),card)
            .on(getOn(directionType,cardId))
            .where(filterByCursorId(cursorId), filterBysuggestionType(suggestionType))
            .orderBy(suggestion.createdDate.desc())
            .limit(size)
            .fetch();

        String nextCursor = suggestionList1.size() < size
            ? null : createCursorId(suggestionList1.get(suggestionList1.size() - 1));

        return new SuggestionListReadPagingResponseDTO(suggestionList1, nextCursor);
    }

    private BooleanExpression filterBysuggestionType(SuggestionType suggestionType) {
        if (suggestionType == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        return suggestion.suggestionType.eq(suggestionType);
    }

    private BooleanExpression filterByCursorId(String cursorId) {
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
            )).lt(cursorId);
    }

    private String createCursorId(SuggestionListReadResponseDTO suggestionListReadResponseDTO) {
        return suggestionListReadResponseDTO.getCreatedAt().toString()    // 디폴트는 생성일자 최신순 정렬
                .replace("T", "")
                .replace("-", "")
                .replace(":", "")
                + String.format("%08d", suggestionListReadResponseDTO.getSuggestionId());
    }

    private QCard getJoin(DirectionType directionType) {
        return switch (directionType) {
            case RECEIVE -> suggestion.toCard;
            case SEND -> suggestion.fromCard;
        };
    }
    private QCard getCounter(DirectionType directionType) {
        return switch (directionType) {
            case RECEIVE -> suggestion.fromCard;
            case SEND -> suggestion.toCard;
        };
    }

    private BooleanExpression getOn(
        DirectionType directionType,
        Long cardId
    ) {
        return switch (directionType) {
            case RECEIVE -> suggestion.toCard.cardId.eq(cardId);
            case SEND -> suggestion.fromCard.cardId.eq(cardId);
        };
    }
}

package org.prgrms.nabimarketbe.domain.suggestion.repository;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.card;
import static org.prgrms.nabimarketbe.domain.suggestion.entity.QSuggestion.suggestion;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardInfo;
import org.prgrms.nabimarketbe.domain.card.entity.QCard;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionDetailResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

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
        List<SuggestionListReadResponseDTO> suggestionList = jpaQueryFactory
            .select(
                Projections.fields(
                    SuggestionListReadResponseDTO.class,
                    Projections.fields(
                        CardInfo.class,
                        getQCardCounter(directionType).cardId,
                        getQCardCounter(directionType).cardTitle,
                        getQCardCounter(directionType).item.itemName,
                        getQCardCounter(directionType).item.priceRange,
                        getQCardCounter(directionType).thumbnail
                    ).as("cardInfo"),
                    Projections.fields(
                        SuggestionDetailResponseDTO.class,
                        suggestion.suggestionId,
                        suggestion.suggestionType,
                        suggestion.suggestionStatus,
                        suggestion.createdDate.as("createdAt"),
                        suggestion.modifiedDate.as("modifiedAt"),
                        Expressions.as(Expressions.constant(directionType),"directionType")
                    ).as("suggestionInfo")
                )
            )
            .from(suggestion)
            .join(getQcardByDirectionType(directionType),card)
            .on(getExpressionByDirectionType(directionType,cardId))
            .where(cursorIdLessThan(cursorId), suggestionTypeEquals(suggestionType))
            .orderBy(suggestion.createdDate.desc())
            .limit(size)
            .fetch();

        String nextCursor = suggestionList.size() < size
            ? null : createCursorId(suggestionList.get(suggestionList.size() - 1));

        return new SuggestionListReadPagingResponseDTO(suggestionList, nextCursor);
    }

    /**
     * 오퍼, 찔러보기 필터링
     */
    private BooleanExpression suggestionTypeEquals(SuggestionType suggestionType) {
        if (suggestionType == null) {
            throw new BaseException(ErrorCode.INVALID_REQUEST);
        }

        return suggestion.suggestionType.eq(suggestionType);
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
                suggestion.createdDate,
                ConstantImpl.create("%Y%m%d%H%i%s")
        );

        return stringTemplate.concat(StringExpressions.lpad(
            suggestion.suggestionId.stringValue(),
            8,
            '0'
        )).lt(cursorId);
    }

    /**
     * 커서 id 생성
     */
    private String createCursorId(SuggestionListReadResponseDTO suggestionListReadResponseDTO) {
        return suggestionListReadResponseDTO.getSuggestionInfo().getCreatedAt().toString()
                .replace("T", "")
                .replace("-", "")
                .replace(":", "")
                + String.format("%08d", suggestionListReadResponseDTO.getSuggestionInfo().getSuggestionId());
    }

    /**
     * 내 카드의 대상이 받은 / 보낸 제안인지에 따른 join 절 분기문 처리
     */
    private QCard getQcardByDirectionType(DirectionType directionType) {
        return switch (directionType) {
            case RECEIVE -> suggestion.toCard;
            case SEND -> suggestion.fromCard;
        };
    }

    /**
     * 받은 / 보낸 제안인지에 따른 Projections.fields 분기문 처리
     * 내가 받은 제안 조회 -> fromCard 필드
     * 내가 보낸 제안 -> toCard 필드
     */
    private QCard getQCardCounter(DirectionType directionType) {
        return switch (directionType) {
            case RECEIVE -> suggestion.fromCard;
            case SEND -> suggestion.toCard;
        };
    }

    /**
     * 받은 / 보낸 제안인지에 따른 on 절 분기문 처리
     */
    private BooleanExpression getExpressionByDirectionType(
        DirectionType directionType,
        Long cardId
    ) {
        return switch (directionType) {
            case RECEIVE -> suggestion.toCard.cardId.eq(cardId);
            case SEND -> suggestion.fromCard.cardId.eq(cardId);
        };
    }
}

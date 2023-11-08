package org.prgrms.nabimarketbe.domain.dibs.repository;

import static org.prgrms.nabimarketbe.domain.card.entity.QCard.*;
import static org.prgrms.nabimarketbe.domain.dibs.entity.QDib.*;
import static org.prgrms.nabimarketbe.domain.item.entity.QItem.*;

import java.util.List;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibListReadResponseDTO;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DibRepositoryImpl implements DibRepositoryCustom{
	public static final int PAGE_SIZE = 10;
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DibListReadResponseDTO> getUserDibsByUserId(
		Long userId,
		Long cursorId
	) {
		List<DibListReadResponseDTO> dibList = jpaQueryFactory.select(
			Projections.fields(
				DibListReadResponseDTO.class,
				card.cardId,
				card.cardTitle,
				item.itemName,
				item.priceRange,
				card.thumbNailImage.as("thumbNail"),
				card.createdDate.as("createdAt"),
				card.modifiedDate.as("modifiedAt")
			)
		)
			.from(dib)
			.innerJoin(card).on(card.cardId.eq(dib.card.cardId))
			.leftJoin(item).on(card.item.itemId.eq(item.itemId))
			.where(
				dibUserIdEquals(userId),
				greaterThan(cursorId)
			)
			.limit(PAGE_SIZE)
			.fetch();

		return dibList;
	}

	private BooleanExpression dibUserIdEquals(Long userId) {
		if(userId == null) {
			return null;
		}

		return dib.user.userId.eq(userId);
	}

	private BooleanExpression greaterThan(Long cursorId) {
		if(cursorId == null) {
			return null;
		}

		return dib.dibId.gt(cursorId);
	}
}

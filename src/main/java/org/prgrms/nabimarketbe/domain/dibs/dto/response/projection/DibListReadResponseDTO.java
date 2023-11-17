package org.prgrms.nabimarketbe.domain.dibs.dto.response.projection;

import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DibListReadResponseDTO {
	private Long dibId;

	private Long cardId;

	private String cardTitle;

	private String itemName;

	private PriceRange priceRange;

	private String thumbNail;

	private CardStatus status;
}

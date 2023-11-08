package org.prgrms.nabimarketbe.domain.dibs.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DibListReadResponseDTO {
	private Long cardId;

	private String cardTitle;

	private String itemName;

	private PriceRange priceRange;

	private String thumbNail;

	private LocalDateTime createdAt;

	private LocalDateTime modifiedAt;
}

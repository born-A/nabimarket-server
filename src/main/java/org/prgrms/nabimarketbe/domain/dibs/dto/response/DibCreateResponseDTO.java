package org.prgrms.nabimarketbe.domain.dibs.dto.response;

import java.time.LocalDateTime;

import org.prgrms.nabimarketbe.domain.dibs.entity.Dib;

import lombok.Builder;

@Builder
public record DibCreateResponseDTO(
	Long dibId,
	Long cardId,
	Long userId,
	LocalDateTime createdAt,
	LocalDateTime modifiedAt
) {

	public static DibCreateResponseDTO from(Dib dib) {
		return DibCreateResponseDTO.builder()
			.dibId(dib.getDibId())
			.userId(dib.getUser().getUserId())
			.cardId(dib.getCard().getCardId())
			.createdAt(dib.getCreatedDate())
			.modifiedAt(dib.getModifiedDate())
			.build();
	}
}

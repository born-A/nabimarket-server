package org.prgrms.nabimarketbe.domain.dibs.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.entity.Dib;
import org.prgrms.nabimarketbe.domain.dibs.repository.DibRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DibService {
	private final DibRepository dibRepository;

	private final UserRepository userRepository;

	private final CardRepository cardRepository;

	private final CheckService checkService;

	public DibCreateResponseDTO createDib(
		String token,
		Long cardId
	) {
		Long userId = checkService.parseToken(token);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new RuntimeException("해당 카드가 없습니다."));

		Dib dib = new Dib(user, card);
		Dib savedDib = dibRepository.save(dib);

		return DibCreateResponseDTO.from(dib);
	}
}

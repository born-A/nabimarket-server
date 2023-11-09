package org.prgrms.nabimarketbe.domain.dibs.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.entity.Dib;
import org.prgrms.nabimarketbe.domain.dibs.repository.DibRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DibService {
	private final DibRepository dibRepository;

	private final UserRepository userRepository;

	private final CardRepository cardRepository;

	private final CheckService checkService;

	@Transactional
	public DibResponseDTO<DibCreateResponseDTO> createDib(
		String token,
		Long cardId
	) {
		Long userId = checkService.parseToken(token);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new RuntimeException("해당 카드가 없습니다."));

		if(checkService.isEqual(token, card.getUser().getUserId())) {
			throw new RuntimeException("자신의 카드는 찜할 수 없습니다.");
		}


		if (dibRepository.existsDibByCardAndUser(card, user)) {
			throw new RuntimeException("이미 찜한 카드는 또 찜할 수 없습니다.");
		}

		Dib dib = new Dib(user, card);
		Dib savedDib = dibRepository.save(dib);

		card.increaseDibCount();

		DibCreateResponseDTO dibCreateResponseDTO = DibCreateResponseDTO.from(savedDib);

		return new DibResponseDTO<>(dibCreateResponseDTO);
	}

	@Transactional
	public void deleteDib(
		String token,
		Long cardId
	) {
		Long userId = checkService.parseToken(token);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new RuntimeException("해당 카드가 없습니다."));

		Dib dib = dibRepository.findDibByUserAndCard(user, card)
			.orElseThrow(() -> new RuntimeException("해당 찜이 존재하지 않습니다."));

		dibRepository.delete(dib);

		card.decreaseDibCount();
	}

	@Transactional(readOnly = true)
	public DibListReadPagingResponseDTO getUserDibsByUserId(
		String token,
		Long cursorId,
		Integer size
	) {
		Long userId = checkService.parseToken(token);
		if (!userRepository.existsById(userId)) {
			throw new RuntimeException("해당 회원이 없습니다.");
		}

		DibListReadPagingResponseDTO dibListReadPagingResponseDTO = dibRepository.getUserDibsByUserId(
			userId,
			cursorId,
			size
		);

		return dibListReadPagingResponseDTO;
	}
}
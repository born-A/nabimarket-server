package org.prgrms.nabimarketbe.domain.dibs.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.wrapper.DibListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.wrapper.DibResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.entity.Dib;
import org.prgrms.nabimarketbe.domain.dibs.repository.DibRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
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
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

		if(checkService.isEqual(userId, card.getUser().getUserId())) {
			throw new BaseException(ErrorCode.DIB_MYSELF_ERROR);
		}

		if (dibRepository.existsDibByCardAndUser(card, user)) {
			throw new BaseException(ErrorCode.DIB_DUPLICATE_ERROR);
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
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

		Card card = cardRepository.findById(cardId)
			.orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

		Dib dib = dibRepository.findDibByUserAndCard(user, card)
			.orElseThrow(() -> new BaseException(ErrorCode.DIB_NOT_FOUND));

		dibRepository.delete(dib);

		card.decreaseDibCount();
	}

	@Transactional(readOnly = true)
	public DibListReadPagingResponseDTO getUserDibsByDibId(
		String token,
		Long cursorId,
		Integer size
	) {
		Long userId = checkService.parseToken(token);
		if (!userRepository.existsById(userId)) {
			throw new BaseException(ErrorCode.USER_NOT_FOUND);
		}

		DibListReadPagingResponseDTO dibListReadPagingResponseDTO = dibRepository.getUserDibsByDibId(
			userId,
			cursorId,
			size
		);

		return dibListReadPagingResponseDTO;
	}
}

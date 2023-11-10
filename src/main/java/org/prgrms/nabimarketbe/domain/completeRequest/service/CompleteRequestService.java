package org.prgrms.nabimarketbe.domain.completeRequest.service;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.request.CompleteRequestDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.CompleteRequestResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequest;
import org.prgrms.nabimarketbe.domain.completeRequest.repository.CompleteRequestRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteRequestService {
    private final CompleteRequestRepository completeRequestRepository;

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final CheckService checkService;

    @Transactional
    public CompleteRequestResponseDTO createCompleteRequest(
        String token,
        CompleteRequestDTO requestDTO
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findByCardIdAndUser(requestDTO.fromCardId(), user)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        Card toCard = cardRepository.findById(requestDTO.toCardId())
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        CompleteRequest completeRequest = CompleteRequest.builder()
            .fromCard(fromCard)
            .toCard(toCard)
            .build();

        CompleteRequest savedCompleteRequest = completeRequestRepository.save(completeRequest);

        return CompleteRequestResponseDTO.of(savedCompleteRequest);
    }

    @Transactional
    public CompleteRequestResponseDTO updateCompleteRequestStatus(
        String token,
        Long fromCardId,
        Long toCardId,
        Boolean isAccepted
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findById(fromCardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Card toCard = cardRepository.findByCardIdAndUser(toCardId, user)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        CompleteRequest completeRequest = completeRequestRepository
            .findCompleteRequestByFromCardAndToCard(fromCard, toCard)
            .orElseThrow(() -> new BaseException(ErrorCode.COMPLETE_REQUEST_NOT_FOUND));

        if (isAccepted) {
            completeRequest.acceptCompleteRequest();
            fromCard.tradeCompleted();
            toCard.tradeCompleted();
        } else {
            completeRequest.refuseCompleteRequest();
        }

        return CompleteRequestResponseDTO.of(completeRequest);
    }

    @Transactional(readOnly = true)
    public HistoryListReadLimitResponseDTO getHistoryBySize(Integer size) {
        return completeRequestRepository.getHistoryBySize(size);
    }

    @Transactional(readOnly = true)
    public HistoryListReadPagingResponseDTO getHistoryByUser(
        String token,
        String cursorId,
        Integer size
    ) {
        User user = userRepository.findById(checkService.parseToken(token))
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        return completeRequestRepository.getHistoryByUser(user, cursorId, size);
    }
}

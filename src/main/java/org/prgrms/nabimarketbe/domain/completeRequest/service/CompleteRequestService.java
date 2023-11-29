package org.prgrms.nabimarketbe.domain.completeRequest.service;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardCondenseResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.request.CompleteRequestDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.CompleteRequestResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.CompleteRequestInfoDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.CompleteRequestSummaryDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadLimitResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.dto.response.wrapper.HistoryListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.completeRequest.entity.CompleteRequest;
import org.prgrms.nabimarketbe.domain.completeRequest.repository.CompleteRequestRepository;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.domain.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.domain.user.dto.response.UserIdResponseDTO;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;

import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.prgrms.nabimarketbe.global.event.NotificationCreateEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompleteRequestService {
    private final CompleteRequestRepository completeRequestRepository;

    private final SuggestionRepository suggestionRepository;

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final CheckService checkService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public CompleteRequestResponseDTO createCompleteRequest(
        String token,
        CompleteRequestDTO requestDTO
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findByCardIdAndUser(requestDTO.fromCardId(), user)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        Card toCard = cardRepository.findActiveCardById(requestDTO.toCardId())
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Suggestion suggestion = suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard)
            .orElseThrow(() -> new BaseException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (completeRequestRepository.exists(fromCard, toCard)) {
            throw new BaseException(ErrorCode.COMPLETE_REQUEST_EXISTS);
        }

        if (completeRequestRepository.existsByFromCard(fromCard)) {
            throw new BaseException(ErrorCode.COMPLETE_REQUEST_FROM_CARD_EXISTS);
        }

        if (completeRequestRepository.existsByToCard(toCard)) {
            throw new BaseException(ErrorCode.COMPLETE_REQUEST_TO_CARD_EXISTS);
        }

        if (fromCard.getStatus() == CardStatus.TRADE_COMPLETE || toCard.getStatus() == CardStatus.TRADE_COMPLETE) {
            throw new BaseException(ErrorCode.CARD_TRADE_COMPLETE);
        }

        if (checkService.isEqual(user.getUserId(), toCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.COMPLETE_REQUEST_MYSELF_ERROR);
        }

        if (!suggestion.isAccepted()) {
            throw new BaseException(ErrorCode.SUGGESTION_NOT_ACCEPTED);
        }

        CompleteRequest completeRequest = new CompleteRequest(fromCard, toCard);

        CompleteRequest savedCompleteRequest = completeRequestRepository.save(completeRequest);

        createCompleteRequestEvent(savedCompleteRequest);

        return CompleteRequestResponseDTO.from(savedCompleteRequest);
    }

    @Transactional
    public CompleteRequestResponseDTO updateCompleteRequestStatus(
        String token,
        Long fromCardId,
        Long toCardId,
        Boolean isAccepted
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findExistingCardById(fromCardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Card toCard = cardRepository.findByCardIdAndUser(toCardId, user)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));

        CompleteRequest completeRequest = completeRequestRepository
            .findCompleteRequestByFromCardAndToCard(fromCard, toCard)
            .orElseThrow(() -> new BaseException(ErrorCode.COMPLETE_REQUEST_NOT_FOUND));

        if (!checkService.isEqual(userId, fromCard.getUser().getUserId()) &&
            !checkService.isEqual(userId, toCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        if (!fromCard.getIsActive()) {
            completeRequest.deleteCompleteRequest();
        } else {
            updateStatus(isAccepted, completeRequest, fromCard, toCard);
            createCompleteRequestDecisionEvent(completeRequest, isAccepted);

        }

        return CompleteRequestResponseDTO.from(completeRequest);
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
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        return completeRequestRepository.getHistoryByUser(user, cursorId, size);
    }

    @Transactional(readOnly = true)
    public CompleteRequestInfoDTO getCompleteRequestById(
        String token,
        Long completeRequestId
    ) {
        Long userId = checkService.parseToken(token);
        if (!userRepository.existsById(userId))
            throw new BaseException(ErrorCode.USER_NOT_FOUND);

        CompleteRequest completeRequest = completeRequestRepository.findById(completeRequestId)
            .orElseThrow(() -> new BaseException(ErrorCode.COMPLETE_REQUEST_NOT_FOUND));

        CompleteRequestSummaryDTO cardUserSummaryResponseDTO = getCardUserSummaryResponseDTO(completeRequest);

        return new CompleteRequestInfoDTO(cardUserSummaryResponseDTO);
    }

    private CompleteRequestSummaryDTO getCardUserSummaryResponseDTO(CompleteRequest completeRequest) {
        Card fromCard = completeRequest.getFromCard();
        CardCondenseResponseDTO fromCardCondenseResponseDTO = CardCondenseResponseDTO.from(fromCard);

        UserIdResponseDTO fromUserIdResponseDTO = new UserIdResponseDTO(fromCard.getUser().getUserId());

        CardUserSummaryResponseDTO fromCardResponseDTO = new CardUserSummaryResponseDTO(
            fromCardCondenseResponseDTO,
            fromUserIdResponseDTO
        );

        Card toCard = completeRequest.getToCard();
        CardCondenseResponseDTO toCardCondenseResponseDTO = CardCondenseResponseDTO.from(toCard);

        UserIdResponseDTO toUserIdResponseDTO = new UserIdResponseDTO(toCard.getUser().getUserId());

        CardUserSummaryResponseDTO toCardResponseDTO = new CardUserSummaryResponseDTO(
            toCardCondenseResponseDTO,
            toUserIdResponseDTO
        );

        return CompleteRequestSummaryDTO.builder()
            .fromCard(fromCardResponseDTO)
            .toCard(toCardResponseDTO)
            .status(completeRequest.getCompleteRequestStatus())
            .build();
    }

    private void updateStatus(
        Boolean isAccepted,
        CompleteRequest completeRequest,
        Card fromCard,
        Card toCard
    ) {
        if (isAccepted) {
            completeRequest.acceptCompleteRequest();
            fromCard.updateCardStatusToTradeComplete();
            toCard.updateCardStatusToTradeComplete();
        } else {
            completeRequest.refuseCompleteRequest();
        }
    }

    private void createCompleteRequestEvent(CompleteRequest completeRequest) {
        User receiver = completeRequest.getToCard().getUser();
        String message = completeRequest.createCompleteRequestMessage();
        applicationEventPublisher.publishEvent(new NotificationCreateEvent(
            receiver,
            completeRequest.getToCard(),
            message
        ));
    }

    private void createCompleteRequestDecisionEvent(CompleteRequest completeRequest, boolean isAccepted) {
        User receiver = completeRequest.getFromCard().getUser();
        String message = completeRequest.createCompleteRequestDecisionMessage(isAccepted);
        applicationEventPublisher.publishEvent(new NotificationCreateEvent(
            receiver,
            completeRequest.getFromCard(),
            message
        ));
    }
}

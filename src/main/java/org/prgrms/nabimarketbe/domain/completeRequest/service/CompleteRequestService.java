package org.prgrms.nabimarketbe.domain.completeRequest.service;

import org.prgrms.nabimarketbe.domain.card.dto.response.CardCondenseResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserSummaryResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteRequestService {
    private final CompleteRequestRepository completeRequestRepository;

    private final SuggestionRepository suggestionRepository;

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final CheckService checkService;

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

        Card toCard = cardRepository.findById(requestDTO.toCardId())
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Suggestion suggestion = suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard)
            .orElseThrow(() -> new BaseException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (!suggestion.isAccepted()) {
            throw new BaseException(ErrorCode.SUGGESTION_NOT_ACCEPTED);
        }

        if (checkService.isEqual(user.getUserId(), toCard.getUser().getUserId())) {
            throw new BaseException(ErrorCode.COMPLETE_REQUEST_MYSELF_ERROR);
        }

        CompleteRequest completeRequest = new CompleteRequest(fromCard, toCard);

        CompleteRequest savedCompleteRequest = completeRequestRepository.save(completeRequest);

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

        Card fromCard = cardRepository.findById(fromCardId)
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

        updateStatus(isAccepted, completeRequest, fromCard, toCard);

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

        List<CardUserSummaryResponseDTO> cardUserSummaryResponseDTOS = getCardUserSummaryResponseDTO(completeRequest);

        CompleteRequestSummaryDTO completeRequestSummaryDTO = new CompleteRequestSummaryDTO(
            cardUserSummaryResponseDTOS.get(0),
            cardUserSummaryResponseDTOS.get(1),
            completeRequest.getCompleteRequestStatus()
        );

        return new CompleteRequestInfoDTO(completeRequestSummaryDTO);
    }

    private List<CardUserSummaryResponseDTO> getCardUserSummaryResponseDTO(CompleteRequest completeRequest) {
        List<CardUserSummaryResponseDTO> cardUserSummaryResponseDTOS = new ArrayList<>();

        Card fromCard = completeRequest.getFromCard();
        CardCondenseResponseDTO fromCardCondenseResponseDTO = CardCondenseResponseDTO.from(fromCard);

        UserIdResponseDTO fromUserIdResponseDTO = new UserIdResponseDTO(fromCard.getUser().getUserId());

        CardUserSummaryResponseDTO fromCardResponseDTO = new CardUserSummaryResponseDTO(
            fromCardCondenseResponseDTO,
            fromUserIdResponseDTO
        );

        cardUserSummaryResponseDTOS.add(0,fromCardResponseDTO);

        Card toCard = completeRequest.getToCard();
        CardCondenseResponseDTO toCardCondenseResponseDTO = CardCondenseResponseDTO.from(toCard);

        UserIdResponseDTO toUserIdResponseDTO = new UserIdResponseDTO(toCard.getUser().getUserId());

        CardUserSummaryResponseDTO toCardResponseDTO = new CardUserSummaryResponseDTO(
            toCardCondenseResponseDTO,
            toUserIdResponseDTO
        );

        cardUserSummaryResponseDTOS.add(1,toCardResponseDTO);

        return cardUserSummaryResponseDTOS;
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
}

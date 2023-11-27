package org.prgrms.nabimarketbe.domain.suggestion.service;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.chatroom.service.ChatRoomService;
import org.prgrms.nabimarketbe.domain.suggestion.dto.request.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.projection.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;
import org.prgrms.nabimarketbe.global.event.NotificationCreateEvent;
import org.prgrms.nabimarketbe.global.redisson.DistributedLock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final UserRepository userRepository;

    private final CheckService checkService;

    private final CardRepository cardRepository;

    private final SuggestionRepository suggestionRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ChatRoomService chatRoomService;

    @Transactional
    @DistributedLock(key = "#lockName")
    public SuggestionResponseDTO createSuggestion(
        String token,
        String lockName,
        String suggestionType,
        SuggestionRequestDTO requestDto
    ) {
        Long userId = checkService.parseToken(token);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findByCardIdAndUser(requestDto.fromCardId(), user)
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_MATCHED));


        Card toCard = cardRepository.findById(requestDto.toCardId())
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (isAuthorEquals(fromCard, toCard)) {
            throw new BaseException(ErrorCode.CARD_SUGGESTION_MYSELF_ERROR);
        }

        if (suggestionRepository.exists(fromCard, toCard)) {
            throw new BaseException(ErrorCode.SUGGESTION_EXISTS);
        }

        if (isExist(fromCard, toCard)) {
            throw new BaseException(ErrorCode.SUGGESTION_EXISTS);
        }

        SuggestionType suggestionTypeEnum = SuggestionType.valueOf(suggestionType);

        if (!suggestionTypeEnum.isSuggestionAvailable(fromCard.getItem(), toCard.getItem())) {
            throw new BaseException(ErrorCode.SUGGESTION_TYPE_MISMATCH);
        }

        if (suggestionTypeEnum.equals(SuggestionType.POKE)) {
            pokeValidation(toCard);
        }

        Suggestion suggestion = Suggestion.builder()
            .suggestionType(suggestionTypeEnum)
            .fromCard(fromCard)
            .toCard(toCard)
            .build();

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);
        createSuggestionEvent(savedSuggestion);

        return SuggestionResponseDTO.from(savedSuggestion);
    }

    private boolean isExist(Card fromCard, Card toCard) {
        return suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard).isPresent()
            || suggestionRepository.findSuggestionByFromCardAndToCard(toCard, fromCard).isPresent();
    }

    @Transactional(readOnly = true)
    public SuggestionListReadPagingResponseDTO getSuggestionsByType(
        String token,
        DirectionType directionType,
        SuggestionType suggestionType,
        Long cardId,
        String cursorId,
        Integer size
    ) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        if (!checkService.isEqual(token, card.getUser().getUserId())) {
            throw new BaseException(ErrorCode.USER_NOT_MATCHED);
        }

        return suggestionRepository.getSuggestionsByType(
            directionType,
            suggestionType,
            card.getCardId(),
            cursorId,
            size
        );
    }

    @Transactional
    public SuggestionResponseDTO updateSuggestionStatus(
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

        Suggestion suggestion = suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard)
            .orElseThrow(() -> new BaseException(ErrorCode.SUGGESTION_NOT_FOUND));

        suggestion.decideSuggestion(isAccepted);

        createSuggestionDecisionEvent(suggestion, isAccepted);

        if (isAccepted) {
            chatRoomService.createChatRoom(suggestion);
        }

        return SuggestionResponseDTO.from(suggestion);
    }

    private boolean isAuthorEquals(
        Card fromCard,
        Card toCard
    ) {
        return fromCard.getUser().getUserId().equals(toCard.getUser().getUserId());
    }

    private void pokeValidation(Card toCard) {
        if (!toCard.isPokeAvailable()) {
            throw new BaseException(ErrorCode.SUGGESTION_TYPE_MISMATCH);
        }
    }

    private void createSuggestionEvent(Suggestion suggestion) {
        User receiver = suggestion.getToCard().getUser();
        String message = suggestion.createSuggestionRequestMessage(suggestion.getFromCard().getUser());
        applicationEventPublisher.publishEvent(new NotificationCreateEvent(
            receiver,
            suggestion.getFromCard(),
            message
        ));
    }

    private void createSuggestionDecisionEvent(Suggestion suggestion, boolean isAccepted) {
        User receiver = suggestion.getFromCard().getUser();
        String message = suggestion.createSuggestionDecisionMessage(isAccepted);
        applicationEventPublisher.publishEvent(new NotificationCreateEvent(
            receiver,
            suggestion.getFromCard(),
            message
        ));
    }
}

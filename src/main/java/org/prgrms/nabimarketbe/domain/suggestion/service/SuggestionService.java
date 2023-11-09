package org.prgrms.nabimarketbe.domain.suggestion.service;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.suggestion.dto.request.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.response.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.DirectionType;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.repository.SuggestionRepository;
import org.prgrms.nabimarketbe.domain.user.entity.User;
import org.prgrms.nabimarketbe.domain.user.repository.UserRepository;
import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.error.BaseException;
import org.prgrms.nabimarketbe.global.error.ErrorCode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final UserRepository userRepository;

    private final CheckService checkService;

    private final CardRepository cardRepository;

    private final SuggestionRepository suggestionRepository;

    @Transactional
    public SuggestionResponseDTO createSuggestion(
            String token,
            String type,
            SuggestionRequestDTO requestDto
    ) {
        boolean existsById = userRepository.existsById(checkService.parseToken(token));

        if(!existsById) throw new BaseException(ErrorCode.USER_NOT_FOUND);

        Card fromCard = cardRepository.findById(requestDto.fromCardId())
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Card toCard = cardRepository.findById(requestDto.toCardId())
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Suggestion suggestion = Suggestion.builder()
                .suggestionType(SuggestionType.valueOf(type))
                .fromCard(fromCard)
                .toCard(toCard)
                .suggestionStatus(SuggestionStatus.WAITING)
                .build();

        Suggestion savedSuggestion = suggestionRepository.save(suggestion);

        return SuggestionResponseDTO.of(savedSuggestion);
    }

    @Transactional(readOnly = true)
    public SuggestionListReadPagingResponseDTO getSuggestionsByType(
            String token,
            DirectionType directionType,
            SuggestionType suggestionType,
            Long cardId,
            String cursorId,
            Integer size
    ){
        User user = userRepository.findById(checkService.parseToken(token))
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        // TODO : cardRepository.findByCardAndUser(card, user)

        return suggestionRepository.getSuggestionsByType(
            directionType,
            suggestionType,
            cardId,
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
        User user = userRepository.findById(checkService.parseToken(token))
            .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new BaseException(ErrorCode.CARD_NOT_FOUND));

        // TODO : cardRepository.findByCardAndUser(toCard, user)

        Suggestion suggestion = suggestionRepository.findSuggestionByFromCardAndToCard(fromCard, toCard)
                .orElseThrow(() -> new BaseException(ErrorCode.SUGGESTION_NOT_FOUND));

        if (isAccepted) {
            suggestion.updateSuggestionStatus(SuggestionStatus.ACCEPTED);
        } else {
            suggestion.updateSuggestionStatus(SuggestionStatus.REFUSED);
        }

        //TODO : 채팅방 생성

        return SuggestionResponseDTO.of(suggestion);
    }
}

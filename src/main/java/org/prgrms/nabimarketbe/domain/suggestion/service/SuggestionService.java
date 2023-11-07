package org.prgrms.nabimarketbe.domain.suggestion.service;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.entity.Card;
import org.prgrms.nabimarketbe.domain.card.repository.CardRepository;
import org.prgrms.nabimarketbe.domain.suggestion.dto.SuggestionRequestDTO;
import org.prgrms.nabimarketbe.domain.suggestion.dto.SuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionStatus;
import org.prgrms.nabimarketbe.domain.suggestion.entity.SuggestionType;
import org.prgrms.nabimarketbe.domain.suggestion.repository.SuggestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final CardRepository cardRepository;

    private final SuggestionRepository suggestionRepository;

    @Transactional
    public SuggestionResponseDTO createSuggestion(String type, SuggestionRequestDTO requestDto) {
        Card fromCard = cardRepository.findById(requestDto.fromCardId())
                .orElseThrow(() -> new RuntimeException("fromCard를 찾지 못했습니다."));

        Card toCard = cardRepository.findById(requestDto.toCardId())
                .orElseThrow(() -> new RuntimeException("toCard를 찾지 못했습니다."));

        Suggestion suggestion1 = Suggestion.builder()
                .suggestionType(SuggestionType.valueOf(type))
                .fromCard(fromCard)
                .toCard(toCard)
                .suggestionStatus(SuggestionStatus.WAITING)
                .isFrom(true)
                .build();

        Suggestion suggestion2 = Suggestion.builder()
                .suggestionType(SuggestionType.valueOf(type))
                .fromCard(toCard)
                .toCard(fromCard)
                .suggestionStatus(SuggestionStatus.WAITING)
                .isFrom(false)
                .build();

        fromCard.getSuggestionList().add(suggestion1);
        toCard.getSuggestionList().add(suggestion2);

        suggestionRepository.save(suggestion1);
        suggestionRepository.save(suggestion2);

        suggestion1.setCounterSuggestionId(suggestion2.getSuggestionId());
        suggestion2.setCounterSuggestionId(suggestion1.getSuggestionId());

        return SuggestionResponseDTO.of(
                SuggestionType.valueOf(type),
                fromCard.getCardId(),
                toCard.getCardId(),
                SuggestionStatus.WAITING
        );
    }
}

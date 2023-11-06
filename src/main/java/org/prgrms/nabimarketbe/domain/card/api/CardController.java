package org.prgrms.nabimarketbe.domain.card.api;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<SingleResult<CardCreateResponseDTO>> createCard(
            @RequestBody CardCreateRequestDTO cardCreateRequestDTO
    ) {
        CardCreateResponseDTO cardCreateResponseDTO = cardService.createCard(cardCreateRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardCreateResponseDTO));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardSingleReadResponseDTO>> getCardById(@PathVariable Long cardId) {
        CardSingleReadResponseDTO cardSingleReadResponseDTO = cardService.getCardById(cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
    }

    @GetMapping
    public ResponseEntity<SingleResult<CardListReadPagingResponseDTO>> getCardsByCondition(
            @RequestParam(required = false) CategoryEnum category,
            @RequestParam(required = false) PriceRange priceRange,
            @RequestParam(required = false) List<CardStatus> status,
            @RequestParam(required = false) String cardTitle,
            @RequestParam(required = false) String cursorId,
            @RequestParam Integer size
    ) {
        CardListReadPagingResponseDTO cardListReadPagingResponseDTO = cardService.getCardsByCondition(
                category,
                priceRange,
                status,
                cardTitle,
                cursorId,
                size
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListReadPagingResponseDTO));
    }
}

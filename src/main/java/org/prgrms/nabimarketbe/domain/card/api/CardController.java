package org.prgrms.nabimarketbe.domain.card.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping(
            consumes = {
                    MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SingleResult<CardCreateResponseDTO>> createCard(
            @RequestHeader(name = "Authorization") String token,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("dto") CardCreateRequestDTO cardCreateRequestDTO,
            @RequestPart("files") List<MultipartFile> files
    ) {
        CardCreateResponseDTO cardCreateResponseDTO = cardService.createCard(
                token,
                cardCreateRequestDTO,
                thumbnail,
                files
        );

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

    @GetMapping("/{cardId}/available-cards")
    public ResponseEntity<SingleResult<CardListResponseDTO<SuggestionAvailableCardResponseDTO>>> getSuggestionAvailableCards(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long cardId
    ) {
        CardListResponseDTO<SuggestionAvailableCardResponseDTO> cardListResponseDTO
                = cardService.getSuggestionAvailableCards(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListResponseDTO));
    }
}

package org.prgrms.nabimarketbe.domain.card.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.SuggestionAvailableCardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Parameter(
        name = "Authorization",
        description = "로그인 성공 후 AccessToken",
        required = true,
        schema = @Schema(type = "string"),
        in = ParameterIn.HEADER)
    @PostMapping
    public ResponseEntity<SingleResult<CardResponseDTO<CardCreateResponseDTO>>> createCard(
            @RequestHeader("Authorization") String token,
            @RequestBody CardCreateRequestDTO cardCreateRequestDTO
    ) {
        CardResponseDTO<CardCreateResponseDTO> card = cardService.createCard(token, cardCreateRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(card));
    }

//    @GetMapping("/{cardId}")
//    public ResponseEntity<SingleResult<CardSingleReadResponseDTO>> getCardById(@PathVariable Long cardId) {
//        CardSingleReadResponseDTO cardSingleReadResponseDTO = cardService.getCardById(cardId);
//
//        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
//    }

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
            @PathVariable(name = "cardId") Long targetCardId
    ) {
        CardListResponseDTO<SuggestionAvailableCardResponseDTO> cardListResponseDTO
                = cardService.getSuggestionAvailableCards(token, targetCardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListResponseDTO));
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardResponseDTO<CardUpdateResponseDTO>>> updateCardById(
        @RequestHeader("Authorization") String token,
        @PathVariable Long cardId,
        @RequestBody CardUpdateRequestDTO cardUpdateRequestDTO
    ) {
        CardResponseDTO<CardUpdateResponseDTO> cardResponseDTO = cardService.updateCardById(
            token,
            cardId,
            cardUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardResponseDTO));
    }

    @PutMapping("/status/{cardId}")
    public ResponseEntity<CommonResult> updateCardStatusById(
            @RequestHeader(name = "authorization") String token,
            @PathVariable Long cardId,
            @RequestBody CardStatusUpdateRequestDTO cardStatusUpdateRequestDTO
    ) {
        cardService.updateCardStatusById(
            token,
            cardId,
            cardStatusUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }

    @GetMapping("/{status}/my-cards")
    public ResponseEntity<SingleResult<CardListReadPagingResponseDTO>> getMyCardsByStatus(
            @RequestHeader(name = "authorization") String token,
            @PathVariable CardStatus status,
            @RequestParam(required = false) String cursorId,
            @RequestParam Integer size
    ) {
        CardListReadPagingResponseDTO cardListReadPagingResponseDTO = cardService.getMyCardsByStatus(
                token,
                status,
                cursorId,
                size
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListReadPagingResponseDTO));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<CommonResult> deleteCardById(
            @RequestHeader(name = "authorization") String token,
            @PathVariable Long cardId
    ) {
        cardService.deleteCardById(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }
}

package org.prgrms.nabimarketbe.domain.card.api;

import java.util.List;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.projection.CardFamousResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardListResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardSuggestionResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.wrapper.CardUserResponseDTO;
import org.prgrms.nabimarketbe.domain.card.entity.CardStatus;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.domain.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.domain.item.entity.PriceRange;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

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

    @GetMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardUserResponseDTO>> getCardById(
        @RequestHeader(value = "Authorization", required = false) String token,
        @PathVariable Long cardId) {
        CardUserResponseDTO cardSingleReadResponseDTO = cardService.getCardById(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
    }

    @GetMapping
    public ResponseEntity<SingleResult<CardPagingResponseDTO>> getCardsByCondition(
        @RequestParam(required = false) CategoryEnum category,
        @RequestParam(required = false) PriceRange priceRange,
        @RequestParam(required = false) List<CardStatus> status,
        @RequestParam(required = false) String cardTitle,
        @RequestParam(required = false) String cursorId,
        @RequestParam Integer size
    ) {
        CardPagingResponseDTO cardListReadPagingResponseDTO = cardService.getCardsByCondition(
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
    public ResponseEntity<SingleResult<CardListResponseDTO<CardSuggestionResponseDTO>>> getSuggestionAvailableCards(
        @RequestHeader(name = "Authorization") String token,
        @PathVariable(name = "cardId") Long targetCardId
    ) {
        CardListResponseDTO<CardSuggestionResponseDTO> cardListResponseDTO
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
    public ResponseEntity<SingleResult<CardPagingResponseDTO>> getMyCardsByStatus(
        @RequestHeader(name = "authorization") String token,
        @PathVariable CardStatus status,
        @RequestParam(required = false) String cursorId,
        @RequestParam Integer size
    ) {
        CardPagingResponseDTO cardListReadPagingResponseDTO = cardService.getMyCardsByStatus(
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

    @GetMapping("/popular")
    public ResponseEntity<SingleResult<CardListResponseDTO<CardFamousResponseDTO>>> getCardsByPopularity(
        @RequestParam Integer size
    ) {
        CardListResponseDTO<CardFamousResponseDTO> cardList = cardService.getCardsByPopularity(size);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardList));
    }
}

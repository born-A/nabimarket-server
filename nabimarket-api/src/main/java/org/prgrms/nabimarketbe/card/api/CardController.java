package org.prgrms.nabimarketbe.card.api;

import java.util.List;

import org.prgrms.nabimarketbe.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.request.CardStatusUpdateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.request.CardUpdateRequestDTO;
import org.prgrms.nabimarketbe.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.CardUpdateResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.wrapper.CardResponseDTO;
import org.prgrms.nabimarketbe.card.dto.response.wrapper.CardUserResponseDTO;

import org.prgrms.nabimarketbe.card.service.CardService;
import org.prgrms.nabimarketbe.model.CommonResult;
import org.prgrms.nabimarketbe.model.ResponseFactory;
import org.prgrms.nabimarketbe.model.SingleResult;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "카드", description = "카드 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @Operation(summary = "카드 등록", description = "카드를 등록합니다.")
    @PostMapping
    public ResponseEntity<SingleResult<CardResponseDTO<CardCreateResponseDTO>>> createCard(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "카드 등록 요청값", required = true)
        @RequestBody CardCreateRequestDTO cardCreateRequestDTO
    ) {
        CardResponseDTO<CardCreateResponseDTO> card = cardService.createCard(token, cardCreateRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(card));
    }

    @Operation(summary = "카드 단건 조회", description = "카드 한 건을 조회합니다.")
    @GetMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardUserResponseDTO>> getCardById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", in = ParameterIn.HEADER)
        @RequestHeader(value = "Authorization", required = false) String token,
        @Parameter(description = "카드 id(pk)", required = true)
        @PathVariable Long cardId
    ) {
        CardUserResponseDTO cardSingleReadResponseDTO = cardService.getCardById(
            token,
            cardId
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
    }

    @Operation(summary = "카드 목록 조회", description = "카드 목록을 조건에 따라 조회합니다.")
    @GetMapping
    public ResponseEntity<SingleResult<CardPagingResponseDTO>> getCardsByCondition(
        @Parameter(description = "카테고리")
        @RequestParam(required = false) CategoryEnumDTO category,
        @Parameter(description = "가격대")
        @RequestParam(required = false) PriceRange priceRange,
        @Parameter(description = "카드 상태(거래 가능, 예약중, 거래 완료)")
        @RequestParam(required = false) List<CardStatus> status,
        @Parameter(description = "카드 제목")
        @RequestParam(required = false) String cardTitle,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) String cursorId,
        @Parameter(description = "page 크기", required = true)
        @RequestParam Integer size
    ) {
        // TODO: 클라이언트에게 정렬 조건 받도록 추후에 수정하면 더 유연할 듯
        OrderCondition condition = OrderCondition.CARD_CREATED_DESC;

        CardPagingResponseDTO cardListReadPagingResponseDTO = cardService.getCardsByCondition(
            category,
            priceRange,
            status,
            cardTitle,
            cursorId,
            size,
            condition
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListReadPagingResponseDTO));
    }

    @Operation(summary = "제안 가능한 내 카드 리슨트 조회", description = "특정 카드에 제안할 수 있는 내 카드 목록을 조회합니다.")
    @GetMapping("/{cardId}/available-cards")
    public ResponseEntity<SingleResult<CardListResponseDTO<CardSuggestionResponseDTO>>> getSuggestionAvailableCards(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "제안 하고 싶은 상대방의 카드 id(pk)", required = true)
        @PathVariable(name = "cardId") Long targetCardId
    ) {
        CardListResponseDTO<CardSuggestionResponseDTO> cardListResponseDTO
            = cardService.getSuggestionAvailableCards(token, targetCardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardListResponseDTO));
    }

    @Operation(summary = "카드 조회 수정 api", description = "자신이 등록한 카드의 정보를 수정합니다.")
    @PutMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardResponseDTO<CardUpdateResponseDTO>>> updateCardById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "자신의 카드 id(pk)", required = true)
        @PathVariable Long cardId,
        @Parameter(description = "카드 수정 요청값", required = true)
        @RequestBody CardUpdateRequestDTO cardUpdateRequestDTO
    ) {
        CardResponseDTO<CardUpdateResponseDTO> cardResponseDTO = cardService.updateCardById(
            token,
            cardId,
            cardUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardResponseDTO));
    }

    @Operation(summary = "카드에 대한 제안 결정", description = "자신의 카드에 들어온 제안에 대해 결정합니다.")
    @PutMapping("/status/{cardId}")
    public ResponseEntity<CommonResult> updateCardStatusById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "자신의 카드 id(pk)", required = true)
        @PathVariable Long cardId,
        @Parameter(description = "카드 제안에 대한 결정 결과값")
        @RequestBody CardStatusUpdateRequestDTO cardStatusUpdateRequestDTO
    ) {
        cardService.updateCardStatusById(
            token,
            cardId,
            cardStatusUpdateRequestDTO
        );

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }

    @Operation(summary = "자신의 카드 목록 조회(상태 기반)", description = "카드 상태에 따른 자신의 카드 목록을 조회합니다.")
    @GetMapping("/{status}/my-cards")
    public ResponseEntity<SingleResult<CardPagingResponseDTO>> getMyCardsByStatus(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "카드 상태", required = true)
        @PathVariable CardStatus status,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) String cursorId,
        @Parameter(description = "page 크기", required = true)
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

    @Operation(summary = "카드 삭제", description = "자신의 카드를 삭제합니다.")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<CommonResult> deleteCardById(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "삭제하고자 하는 자신의 카드 id(pk)", required = true)
        @PathVariable Long cardId
    ) {
        cardService.deleteCardById(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }

    @Operation(summary = "인기 상품 목록 조회", description = "메인 베너에 띄우는 인기 상품 목록을 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<SingleResult<CardListResponseDTO<CardFamousResponseDTO>>> getCardsByPopularity() {
        CardListResponseDTO<CardFamousResponseDTO> cardList = cardService.getCardsByPopularity();

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardList));
    }
}

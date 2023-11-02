package org.prgrms.nabimarketbe.domain.card.api;

import lombok.RequiredArgsConstructor;
import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.global.ResponseFactory;
import org.prgrms.nabimarketbe.global.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<SingleResult<CardCreateResponseDTO>> save(
            @RequestBody CardCreateRequestDTO cardCreateRequestDTO
    ) {
        CardCreateResponseDTO cardCreateResponseDTO = cardService.save(cardCreateRequestDTO);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardCreateResponseDTO));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardSingleReadResponseDTO>> singleRead(@PathVariable Long cardId) {
        CardSingleReadResponseDTO cardSingleReadResponseDTO = cardService.singleRead(cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
    }
}

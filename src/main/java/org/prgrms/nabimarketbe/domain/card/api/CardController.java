package org.prgrms.nabimarketbe.domain.card.api;

import lombok.RequiredArgsConstructor;

import org.prgrms.nabimarketbe.domain.card.dto.request.CardCreateRequestDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.card.dto.response.CardSingleReadResponseDTO;
import org.prgrms.nabimarketbe.domain.card.service.CardService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ResponseEntity<SingleResult<CardCreateResponseDTO>> save(
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("dto") CardCreateRequestDTO cardCreateRequestDTO,
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {
        CardCreateResponseDTO cardCreateResponseDTO = cardService.save(cardCreateRequestDTO,thumbnail,files);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardCreateResponseDTO));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<SingleResult<CardSingleReadResponseDTO>> singleRead(@PathVariable Long cardId) {
        CardSingleReadResponseDTO cardSingleReadResponseDTO = cardService.singleRead(cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(cardSingleReadResponseDTO));
    }

}

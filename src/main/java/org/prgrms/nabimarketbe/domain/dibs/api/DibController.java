package org.prgrms.nabimarketbe.domain.dibs.api;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.service.DibService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/dibs")
@RestController
public class DibController {
	private final DibService dibService;

	@PostMapping("/{cardId}")
	public ResponseEntity<SingleResult<DibResponseDTO<DibCreateResponseDTO>>> createDib(
		@RequestHeader(name = "authorization") String token,
		@PathVariable Long cardId
	) {
		DibCreateResponseDTO dibCreateResponseDTO = dibService.createDib(token, cardId);
		DibResponseDTO dibResponseDTO = new DibResponseDTO(dibCreateResponseDTO);

		return ResponseEntity.ok(ResponseFactory.getSingleResult(dibResponseDTO));
	}
}

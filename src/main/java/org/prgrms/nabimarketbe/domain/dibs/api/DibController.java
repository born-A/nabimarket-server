package org.prgrms.nabimarketbe.domain.dibs.api;

import org.prgrms.nabimarketbe.domain.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.wrapper.DibListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.dto.response.wrapper.DibResponseDTO;
import org.prgrms.nabimarketbe.domain.dibs.service.DibService;
import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.CommonResult;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/dibs")
@RestController
public class DibController {
	private final DibService dibService;

	@PostMapping("/{cardId}")
	public ResponseEntity<SingleResult<DibResponseDTO<DibCreateResponseDTO>>> createDib(
		@RequestHeader(name = "Authorization") String token,
		@PathVariable Long cardId
	) {
		DibResponseDTO<DibCreateResponseDTO> dibResponseDTO = dibService.createDib(token, cardId);

		return ResponseEntity.ok(ResponseFactory.getSingleResult(dibResponseDTO));
	}

	@DeleteMapping("/{cardId}")
	public ResponseEntity<CommonResult> deleteDib(
		@RequestHeader(name = "authorization") String token,
		@PathVariable Long cardId
	) {
		dibService.deleteDib(token, cardId);

		return ResponseEntity.ok(ResponseFactory.getSuccessResult());
	}

	@GetMapping
	public ResponseEntity<SingleResult<DibListReadPagingResponseDTO>> getUserDibsByDibId(
		@RequestHeader(name = "authorization") String token,
		@RequestParam(required = false) Long cursorId,
		@RequestParam Integer size
	) {
		DibListReadPagingResponseDTO dibListReadPagingResponseDTO = dibService.getUserDibsByDibId(
			token,
			cursorId,
			size
		);

		return ResponseEntity.ok(ResponseFactory.getSingleResult(dibListReadPagingResponseDTO));
	}
}

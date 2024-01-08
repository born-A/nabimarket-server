package org.prgrms.nabimarketbe.dibs;

import org.prgrms.nabimarketbe.dibs.dto.response.DibCreateResponseDTO;
import org.prgrms.nabimarketbe.dibs.service.DibService;
import org.prgrms.nabimarketbe.jpa.dibs.projection.DibListReadPagingResponseDTO;
import org.prgrms.nabimarketbe.dibs.dto.wrapper.DibResponseDTO;
import org.prgrms.nabimarketbe.model.CommonResult;
import org.prgrms.nabimarketbe.model.ResponseFactory;
import org.prgrms.nabimarketbe.model.SingleResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "찜", description = "찜 기능 관련 API 입니다.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/dibs")
@RestController
public class DibController {
    private final DibService dibService;

    @Operation(summary = "카드 찜 하기", description = "맘에 드는 카드를 찜할 수 있습니다.")
    @PostMapping("/{cardId}")
    public ResponseEntity<SingleResult<DibResponseDTO<DibCreateResponseDTO>>> createDib(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "Authorization") String token,
        @Parameter(description = "카드 id(pk)", required = true)
        @PathVariable Long cardId
    ) {
        DibResponseDTO<DibCreateResponseDTO> dibResponseDTO = dibService.createDib(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(dibResponseDTO));
    }

    @Operation(summary = "카드 찜 취소", description = "찜한 카드를 취소할 수 있습니다.")
    @DeleteMapping("/{cardId}")
    public ResponseEntity<CommonResult> deleteDib(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "authorization") String token,
        @Parameter(description = "카드 id(pk)", required = true)
        @PathVariable Long cardId
    ) {
        dibService.deleteDib(token, cardId);

        return ResponseEntity.ok(ResponseFactory.getSuccessResult());
    }

    @Operation(summary = "찜 목록 조회", description = "찜한 카드 목록을 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<SingleResult<DibListReadPagingResponseDTO>> getUserDibsByDibId(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader(name = "authorization") String token,
        @Parameter(description = "커서 id")
        @RequestParam(required = false) Long cursorId,
        @Parameter(description = "page 크기", required = true)
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

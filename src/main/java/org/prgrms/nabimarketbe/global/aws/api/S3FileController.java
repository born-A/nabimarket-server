package org.prgrms.nabimarketbe.global.aws.api;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;

import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지 업로드", description = "이미지 업로드를 위한 API 입니다.")
@RestController
@RequestMapping("/api/v1/s3/upload")
@RequiredArgsConstructor
@Slf4j
public class S3FileController {
    private final S3FileUploadService s3FileUploadService;

    private final CheckService checkService;

    @Operation(summary = "이미지 단건 업로드", description = "하나의 이미지 파일을 업로드합니다.")
    @PostMapping("/single")
    public ResponseEntity<SingleResult<String>> uploadFile(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "업로드 하려는 파일(단건)", required = true)
        @RequestPart("file") MultipartFile file
    ) {
        String uploadUrl = s3FileUploadService.uploadFile(file);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(uploadUrl));
    }

    @Operation(summary = "이미지 다건 업로드", description = "여래 개의 이미지 파일을 업로드합니다.")
    @PostMapping("/multi")
    public ResponseEntity<SingleResult<List<String>>> uploadFiles(
        @Parameter(name = "Authorization", description = "로그인 성공 후 AccessToken", required = true, in = ParameterIn.HEADER)
        @RequestHeader("Authorization") String token,
        @Parameter(description = "업로드 하려는 파일(다건)", required = true)
        @RequestPart("files") List<MultipartFile> files
    ) {
        List<String> uploadUrls = s3FileUploadService.uploadFiles(files);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(uploadUrls));
    }
}

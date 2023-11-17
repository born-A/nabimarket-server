package org.prgrms.nabimarketbe.global.aws.api;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.prgrms.nabimarketbe.domain.user.service.CheckService;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;

import org.prgrms.nabimarketbe.global.util.ResponseFactory;
import org.prgrms.nabimarketbe.global.util.model.SingleResult;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3/upload")
@RequiredArgsConstructor
@Slf4j
public class S3FileController {
    private final S3FileUploadService s3FileUploadService;

    private final CheckService checkService;

    @PostMapping("/single")
    public ResponseEntity<SingleResult<String>> uploadFile(
        @RequestHeader("Authorization") String token,
        @RequestPart("file") MultipartFile file
    ) {
        checkService.parseToken(token);
        String uploadUrl = s3FileUploadService.uploadFile(file);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(uploadUrl));
    }

    @PostMapping("/multi")
    public ResponseEntity<SingleResult<List<String>>> uploadFiles(
        @RequestHeader("Authorization") String token,
        @RequestPart("files") List<MultipartFile> files
    ) {
        checkService.parseToken(token);
        List<String> uploadUrls = s3FileUploadService.uploadFiles(files);

        return ResponseEntity.ok(ResponseFactory.getSingleResult(uploadUrls));
    }
}

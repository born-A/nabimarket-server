package org.prgrms.nabimarketbe.global.aws.controller;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.nabimarketbe.global.aws.service.S3FileUploadService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class S3FileController {
    private final S3FileUploadService s3FileUploadService;

    @PostMapping
    public String uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        String url = s3FileUploadService.uploadFile(file);
        return url;
    }

    @GetMapping
    public String findImg() {
        String imgPath = s3FileUploadService.getThumbnailPath("test1/8300a5cd-341f-46bc-8eca-2e79355c7cd4-after.png");
        log.info(imgPath);
        return imgPath;
    }
}
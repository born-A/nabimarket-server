package org.prgrms.nabimarketbe.global.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3FileUploadService {
    private final AmazonS3Client amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String dir;

    private String subDir;

    private String defaultUrl = "https://airplanning-bucket.s3.ap-northeast-2.amazonaws.com";

    public String getThumbnailPath(String path) {
        return amazonS3.getUrl(bucketName, path).toString();
    }

    public String uploadFile(
            String domain,
            Long id,
            MultipartFile file
    ) {
        dir = "/" + domain;
        subDir = "/" + id;

        String bucketDir = bucketName + dir + subDir;
        String dirUrl = defaultUrl + dir + subDir + "/";

        String fileName = generateFileName(file);

        try {
            amazonS3.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));
        } catch (IOException e) {
            throw new RuntimeException("단건 이미지 업로드를 실패하였습니다.");
        }

        return dirUrl + fileName;
    }

    public List<String> uploadFileList(
            String domain,
            Long entityId,
            List<MultipartFile> multipartFiles
    ) {
        List<String> imgUrlList = new ArrayList<>();

        dir = "/" + domain;
        subDir = "/" + entityId;

        String bucketDir = bucketName + dir + subDir;
        String dirUrl = defaultUrl + dir + subDir + "/";

        for (MultipartFile file : multipartFiles) {
            String fileName = generateFileName(file);

            try {
                amazonS3.putObject(new PutObjectRequest(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file)));
            } catch (IOException e) {
                throw new RuntimeException("다건 이미지 업로드를 실패하였습니다.");
            }

            imgUrlList.add(dirUrl + fileName);
        }

        return imgUrlList;
    }

    // 이미지 수정으로 인해 기존 이미지 삭제 메소드
    public void deleteImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        return objectMetadata;
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }
}


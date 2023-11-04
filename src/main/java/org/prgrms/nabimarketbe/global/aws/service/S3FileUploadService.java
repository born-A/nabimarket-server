package org.prgrms.nabimarketbe.global.aws.service;

import com.amazonaws.services.s3.AmazonS3Client;
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

    private String dir = "/test1";

    private String defaultUrl = "https://airplanning-bucket.s3.ap-northeast-2.amazonaws.com";

    public String getThumbnailPath(String path) {
        return amazonS3.getUrl(bucketName, path).toString();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String bucketDir = bucketName + dir;
        String dirUrl = defaultUrl + dir + "/";
        String fileName = generateFileName(file);

        amazonS3.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));

        return dirUrl + fileName;
    }

    public List<String> uploadFileList(List<MultipartFile> multipartFiles) throws IOException {
        List<String> imgUrlList = new ArrayList<>();

        String bucketDir = bucketName + dir;
        String dirUrl = defaultUrl + dir + "/";

        for (MultipartFile file : multipartFiles) {
            String fileName = generateFileName(file);

            amazonS3.putObject(new PutObjectRequest(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file)));
            imgUrlList.add(dirUrl + fileName);
        }

        return imgUrlList;
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


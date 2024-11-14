package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.responses.UploadResponse;
import com.dattran.job_finder_springboot.config.AwsProperties;
import com.dattran.job_finder_springboot.domain.entities.mongo.Storage;
import com.dattran.job_finder_springboot.domain.enums.UploadType;
import com.dattran.job_finder_springboot.domain.repositories.mongo.StorageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StorageService {
  AwsProperties awsProperties;
  StorageRepository storageRepository;
  S3Presigner s3Presigner;

  @NonFinal
  @Value("${aws.expire-time}")
  Integer expireTime;

  public UploadResponse generateUrl(UploadType type, String userId) {
    UUID uuid = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();
    StringBuilder sb = new StringBuilder();
    sb.append(now.format(DateTimeFormatter.ISO_DATE)).append("_").append(uuid);
    String urlUpload = generatePutPresignedUrl(sb.toString());
    String imageUrl =
        "https://"
            + awsProperties.getS3BucketName()
            + ".s3."
            + awsProperties.getRegion()
            + ".amazonaws.com/"
            + sb;
    saveLog(awsProperties.getS3BucketName(), type, imageUrl, userId);
    return UploadResponse.builder()
        .imageUrl(imageUrl)
        .uploadUrl(urlUpload)
        .imagePath("/" + sb)
        .build();
  }

  private void saveLog(String bucket, UploadType type, String imageUrl, String userId) {
    Storage storage =
        Storage.builder()
            .bucket(bucket)
            .state(Storage.State.PUBLIC)
            .createdAt(LocalDateTime.now())
            .type(type)
            .url(imageUrl)
            .userId(userId)
            .isDeleted(false)
            .build();
    storageRepository.save(storage);
  }

  // Khi không bật ACL
  private String createPresignedGetUrl(String key) {
    GetObjectRequest objectRequest =
        GetObjectRequest.builder().bucket(awsProperties.getS3BucketName()).key(key).build();
    GetObjectPresignRequest presignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(expireTime)) // The URL will expire in 30 minutes.
            .getObjectRequest(objectRequest)
            .build();
    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    log.info("Presigned URL: [{}]", presignedRequest.url().toString());
    log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());
    return presignedRequest.url().toExternalForm();
  }

  // Sử dụng khi bật ACL
  private String generatePutPresignedUrl(String filePath) {
    PutObjectRequest.Builder putObjectRequestBuilder =
        PutObjectRequest.builder().bucket(awsProperties.getS3BucketName()).key(filePath);

    putObjectRequestBuilder.acl(ObjectCannedACL.PUBLIC_READ);

    PutObjectRequest putObjectRequest = putObjectRequestBuilder.build();

    PutObjectPresignRequest presignRequest =
        PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(60))
            .putObjectRequest(putObjectRequest)
            .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
    return presignedRequest.url().toString();
  }
}

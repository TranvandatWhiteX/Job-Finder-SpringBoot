package com.dattran.job_finder_springboot.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AwsConfig {
  AwsProperties awsProperties;

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials awsCredsAwsBasicCredentials =
        AwsBasicCredentials.create(
            awsProperties.getAccessKeyId(), awsProperties.getSecretAccessKey());
    return S3Client.builder()
        .region(Region.of(awsProperties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(awsCredsAwsBasicCredentials))
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    S3Configuration s3Config = S3Configuration.builder()
            .checksumValidationEnabled(true)  // Optional: Enable checksum validation
            .build();
    AwsBasicCredentials awsCredsAwsBasicCredentials =
            AwsBasicCredentials.create(
                    awsProperties.getAccessKeyId(), awsProperties.getSecretAccessKey());
    return S3Presigner.builder()
            .region(Region.of(awsProperties.getRegion()))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredsAwsBasicCredentials))
            .serviceConfiguration(s3Config)
            .build();
  }
}

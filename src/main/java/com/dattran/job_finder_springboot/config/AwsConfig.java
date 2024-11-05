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
}

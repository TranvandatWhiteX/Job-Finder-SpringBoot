package com.dattran.job_finder_springboot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Data
public class AwsProperties {
  private String region;

  private String accessKeyId;

  private String secretAccessKey;

  private String s3BucketName;
}

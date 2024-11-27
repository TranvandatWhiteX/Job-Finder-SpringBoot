package com.dattran.job_finder_springboot.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
  @Value("${elasticsearch.host}")
  private String HOST;

  @Value("${elasticsearch.port}")
  private int PORT;

  @Value("${elasticsearch.scheme}")
  private String SCHEME;

  @Bean
  public RestHighLevelClient client() {
    RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT, SCHEME));
    return new RestHighLevelClient(builder);
  }
}

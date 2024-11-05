package com.dattran.job_finder_springboot.domain.utils;

import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
  public HttpStatusCode status;
  public String body;
  public HttpHeaders headers;
}

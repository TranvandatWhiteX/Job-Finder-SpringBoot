package com.dattran.job_finder_springboot.domain.utils;

import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RequestUtil {
  private static RestTemplate restTemplate;
  private static final int TIME_OUT = 5;

  public static RestTemplate getTemplate() {
    if (restTemplate == null) {
      restTemplate = new RestTemplate(getClientHttpRequestFactory(TIME_OUT));
      return restTemplate;
    }
    return restTemplate;
  }

  /**
   * Sends an HTTP request using the specified HTTP method, URL, request body, and other parameters.
   *
   * <p>This method constructs the request URL by replacing path variables and appending query
   * parameters. It then sets the request headers and creates an {@link HttpEntity} based on the
   * provided content type. Finally, it uses a {@link RestTemplate} to send the request and returns
   * an {@link HttpResponse} with the status code, response body, and headers.
   *
   * @param <R> the type of the request body
   * @param method the HTTP method to be used for the request (e.g., GET, POST, PUT, DELETE)
   * @param requestUrl the URL for the HTTP request, which may include path variables and query
   *     parameters
   * @param requestBody the body of the request, which may be null for methods that do not require a
   *     body
   * @param pathVariables a map of path variables to be replaced in the URL
   * @param requestParams a map of query parameters to be appended to the URL
   * @param headers a map of HTTP headers to be included in the request
   * @param contentType the content type of the request (e.g., {@link MediaType#APPLICATION_JSON},
   *     {@link MediaType#APPLICATION_FORM_URLENCODED})
   * @return an {@link HttpResponse} object containing the status code, response body, and headers
   * @throws RuntimeException if an error occurs while sending the request or processing the
   *     response
   */
  public static <R> HttpResponse sendRequest(
      HttpMethod method,
      String requestUrl,
      R requestBody,
      Map<String, String> pathVariables,
      Map<String, String> requestParams,
      Map<String, String> headers,
      MediaType contentType) {
    try {
      if (!pathVariables.isEmpty()) {
        for (Map.Entry<String, String> entry : pathVariables.entrySet()) {
          String pathVariable = String.format("{%s}", entry.getKey());
          requestUrl = requestUrl.replace(pathVariable, entry.getValue());
        }
      }
      if (!requestParams.isEmpty() && requestBody != null) {
        List<String> keyAndVal =
            requestParams.entrySet().stream()
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .toList();
        requestUrl = requestUrl + "?" + String.join("&", keyAndVal);
      }
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(contentType);
      if (!headers.isEmpty()) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
          httpHeaders.add(entry.getKey(), entry.getValue());
        }
      }
      HttpEntity<?> httpEntity;
      if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
        Map<String, Object> formData = JsonParser.objectToMap(requestBody);
        httpEntity = new HttpEntity<>(formData, httpHeaders);
      } else {
        httpEntity = new HttpEntity<>(requestBody, httpHeaders);
      }
      RestTemplate restTemplate = getTemplate();
      ResponseEntity<String> response =
          restTemplate.exchange(requestUrl, method, httpEntity, String.class);
      // Todo: Log Calling API
      return new HttpResponse(response.getStatusCode(), response.getBody(), response.getHeaders());
    } catch (Exception e) {
      String msg = String.format("Cannot call api: %s, error: %s", requestUrl, e.getMessage());
      throw new RuntimeException(msg);
    }
  }

  /**
   * Creates and configures an {@link HttpComponentsClientHttpRequestFactory} that accepts all SSL
   * certificates and sets a configurable connection timeout.
   *
   * @param timeOut the connection timeout in seconds used for HTTP requests.
   * @return a configured {@link HttpComponentsClientHttpRequestFactory} that can be used for HTTP
   *     connections.
   * @throws RuntimeException if any error occurs during SSL context creation or setting up the HTTP
   *     client.
   */
  private static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(int timeOut) {
    try {
      TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;

      SSLContext sslcontext =
          SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

      SSLConnectionSocketFactory sslSocketFactory =
          SSLConnectionSocketFactoryBuilder.create().setSslContext(sslcontext).build();

      HttpClientConnectionManager cm =
          PoolingHttpClientConnectionManagerBuilder.create()
              .setSSLSocketFactory(sslSocketFactory)
              .build();

      CloseableHttpClient httpClient =
          HttpClients.custom().setConnectionManager(cm).evictExpiredConnections().build();

      HttpComponentsClientHttpRequestFactory requestFactory =
          new HttpComponentsClientHttpRequestFactory();
      requestFactory.setConnectTimeout(timeOut * 1000);
      requestFactory.setHttpClient(httpClient);

      return requestFactory;
    } catch (Exception var6) {
      throw new RuntimeException(var6);
    }
  }
}

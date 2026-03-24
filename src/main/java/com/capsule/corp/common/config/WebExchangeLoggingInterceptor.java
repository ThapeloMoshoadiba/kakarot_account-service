package com.capsule.corp.common.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class WebExchangeLoggingInterceptor implements ClientHttpRequestInterceptor {

  public WebExchangeLoggingInterceptor() {}

  @Override
  public ClientHttpResponse intercept(
      final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution)
      throws IOException {
    if (log.isTraceEnabled()) {
      logRequest(request, body);
      // execute request response exchange
      ClientHttpResponse response = execution.execute(request, body);
      // prints after making a copy to stream to client
      return logResponse(response);
    } else {
      // continues chain
      return execution.execute(request, body);
    }
  }

  private void logRequest(final HttpRequest request, final byte[] body) {
    log.trace("Request: {} {}", request.getMethod(), request.getURI());
    if (body != null && body.length > 0) {
      logHeaders(request.getHeaders());
      log.trace("Request body: {}", new String(body, StandardCharsets.UTF_8));
    }
  }

  private ClientHttpResponse logResponse(final ClientHttpResponse response) throws IOException {
    byte[] responseBody = response.getBody().readAllBytes();
    logHeaders(response.getHeaders());
    if (responseBody.length > 0) {
      log.trace("Response body: {}", new String(responseBody, StandardCharsets.UTF_8));
      return new BufferingClientHttpResponseWrapper(response, responseBody);
    } else {
      log.trace("Response body: {}", responseBody);
    }
    return response;
  }

  private void logHeaders(final HttpHeaders headers) {
    headers.entrySet().forEach(h -> log.trace("Header [{}: {}]", h.getKey(), h.getValue()));
  }

  private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

    private final ClientHttpResponse response;
    private final byte[] body;

    @Override
    public InputStream getBody() throws IOException {
      return new ByteArrayInputStream(body);
    }

    BufferingClientHttpResponseWrapper(
        final ClientHttpResponse httpResponse, final byte[] responseBody) {
      this.response = httpResponse;
      this.body = responseBody;
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
      return response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
      return response.getStatusText();
    }

    @Override
    public void close() {
      response.close();
    }

    @Override
    public HttpHeaders getHeaders() {
      return response.getHeaders();
    }
  }
}

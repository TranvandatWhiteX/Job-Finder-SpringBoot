package com.dattran.job_finder_springboot.logging;

import com.dattran.job_finder_springboot.domain.utils.HttpRequestUtil;
import com.dattran.job_finder_springboot.logging.entities.*;
import com.dattran.job_finder_springboot.logging.repositories.LogApiRepository;
import com.dattran.job_finder_springboot.logging.repositories.LogEventRepository;
import com.dattran.job_finder_springboot.logging.repositories.LogExceptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoggingService {
    LogApiRepository logApiRepository;
    LogEventRepository logEventRepository;
    LogExceptionRepository logExceptionRepository;

    public void writeLogApi(String url,
                            HttpMethod method,
                            Object auth,
                            HttpStatus status,
                            Object body,
                            Object response) {
        LogApi logApi = LogApi.builder()
                .auth(auth)
                .url(url)
                .method(method)
                .status(status)
                .body(body)
                .response(response)
                .createdAt(LocalDateTime.now())
                .build();
        logApiRepository.save(logApi);
    }

    public void writeLogException(Integer status,
                              String message,
                              String description,
                              HttpServletRequest request) {
        LogException logException = LogException.builder()
                .status(status)
                .message(message)
                .description(description)
                .path(request.getRequestURI())
                .createdAt(LocalDateTime.now())
                .headers(HttpRequestUtil.getHeaderMap(request))
                .params(request.getParameterMap())
                .body(HttpRequestUtil.getBodyMap(request))
                .build();
        logExceptionRepository.save(logException);
    }

    public void writeLogEvent(String userId,
                              LogAction action,
                              String ip,
                              String objectName,
                              Object preValue,
                              Object value) {
        LogEvent logEvent = LogEvent.builder()
                .userId(userId)
                .action(action)
                .ip(ip)
                .objectName(objectName)
                .preValue(preValue)
                .value(value)
                .createdAt(LocalDateTime.now())
                .build();
        logEventRepository.save(logEvent);
    }
}

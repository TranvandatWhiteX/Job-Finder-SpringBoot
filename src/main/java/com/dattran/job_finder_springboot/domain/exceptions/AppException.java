package com.dattran.job_finder_springboot.domain.exceptions;

import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {
    private ResponseStatus responseStatus;

    public AppException(ResponseStatus responseStatus) {
        super(responseStatus.getMessage());
        this.responseStatus = responseStatus;
    }
}

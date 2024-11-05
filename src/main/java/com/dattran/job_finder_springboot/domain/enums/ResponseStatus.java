package com.dattran.job_finder_springboot.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    USER_NOT_FOUND(404, "User not found"),
    ROLE_NOT_FOUND(404, "Role not found"),
    EMAIL_ALREADY_EXISTED(400, "Email already existed!"),;
    private final int code;
    private final String message;
}

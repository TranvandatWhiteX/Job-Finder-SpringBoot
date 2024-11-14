package com.dattran.job_finder_springboot.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SKILL_NOT_FOUND(404, "Skill not found!"),
    COMPANY_NOT_FOUND(404, "Company not found!"),
    BUSINESS_STREAM_NOT_FOUND(404, "Business Stream Not Found!"),
    TAX_NOT_FOUND(404, "Tax Not Found!"),
    INVALID_TAX(400, "Tax is null or tax already exist!"),
    JWT_ERROR(400, "Jwt Error!"),
    PASSWORD_NOT_MATCH(400, "Password Not Match!"),
    USER_ERROR(400, "Un-active User Or User Deleted!"),
    INVALID_TOKEN(400, "Invalid Token!"),
    USER_NOT_FOUND(404, "User not found!"),
    ROLE_NOT_FOUND(404, "Role not found!"),
    EMAIL_ALREADY_EXISTED(400, "Email already existed!"),;
    private final int code;
    private final String message;
}

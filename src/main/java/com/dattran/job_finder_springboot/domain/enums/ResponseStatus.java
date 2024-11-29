package com.dattran.job_finder_springboot.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    FORBIDDEN(400, "Forbidden!"),
    DATA_EXCEL_FILE_NULL(400, "Data Excel file is null!"),
    JOB_TITLE_MUST_NOT_BE_NULL(400,"Job title must not be null!"),
    INVALID_EXCEL_FILE(400, "Invalid Excel File!"),
    FILE_NOT_FOUND(404, "File Not Found!"),
    FILE_SIZE_EXCEEDED(400, "File exceeded!"),
    INVALID_FILE(400, "Invalid file!"),
    DOWNLOAD_TEMPLATE_ERROR(400, "Download Template Error!"),
    JOB_NOT_FOUND(404, "Job not found!"),
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

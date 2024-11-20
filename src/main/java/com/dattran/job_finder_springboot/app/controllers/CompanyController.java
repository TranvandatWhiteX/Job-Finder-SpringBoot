package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.CompanyDto;
import com.dattran.job_finder_springboot.domain.entities.Company;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.services.CompanyService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/companies")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CompanyController {
    CompanyService companyService;

    @PostMapping
//    @PreAuthorize("hasRole('ADMINISTRATION')")
    public ApiResponse<Company> addCompany(@RequestBody @Valid CompanyDto companyDto,
                                           HttpServletRequest httpServletRequest) {
        Company company = companyService.addCompany(companyDto, httpServletRequest);
        return ApiResponse.<Company>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .result(company)
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .message("Company Created Successfully!")
                .build();
    }
}

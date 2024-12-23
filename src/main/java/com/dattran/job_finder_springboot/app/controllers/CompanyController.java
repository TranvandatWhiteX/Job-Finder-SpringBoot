package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.CompanyDto;
import com.dattran.job_finder_springboot.app.dtos.UpdateCompanyDto;
import com.dattran.job_finder_springboot.domain.entities.Company;
import com.dattran.job_finder_springboot.domain.services.CompanyService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("")
    public ApiResponse<List<Company>> getCompanies(HttpServletRequest httpServletRequest) {
        List<Company> companies = companyService.getCompanies();
        return ApiResponse.<List<Company>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .result(companies)
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .message("Company Created Successfully!")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Company> getCompany(@PathVariable String id, HttpServletRequest httpServletRequest) {
        Company company = companyService.getCompanyById(id);
        return ApiResponse.<Company>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .result(company)
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Get Company Successfully!")
                .build();
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMINISTRATION')")
    public ApiResponse<Company> updateCompany(@PathVariable String id, @RequestBody @Valid UpdateCompanyDto updateCompanyDto, HttpServletRequest httpServletRequest) {
        Company company = companyService.updateCompany(id, updateCompanyDto, httpServletRequest);
        return ApiResponse.<Company>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .result(company)
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Update Company Successfully!")
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMINISTRATION')")
    public ApiResponse<Void> deleteCompany(@PathVariable String id, HttpServletRequest httpServletRequest) {
        companyService.deleteById(id, httpServletRequest);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.OK)
                .message("Delete Company Successfully!")
                .build();
    }
}

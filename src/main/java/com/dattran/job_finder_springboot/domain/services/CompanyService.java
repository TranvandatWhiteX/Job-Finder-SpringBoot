package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.CompanyDto;
import com.dattran.job_finder_springboot.app.dtos.UpdateCompanyDto;
import com.dattran.job_finder_springboot.domain.entities.BusinessStream;
import com.dattran.job_finder_springboot.domain.entities.Company;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.BusinessStreamRepository;
import com.dattran.job_finder_springboot.domain.repositories.CompanyRepository;
import com.dattran.job_finder_springboot.domain.utils.FnCommon;
import com.dattran.job_finder_springboot.domain.utils.HttpRequestUtil;
import com.dattran.job_finder_springboot.domain.utils.HttpResponse;
import com.dattran.job_finder_springboot.domain.utils.RequestUtil;
import com.dattran.job_finder_springboot.logging.LoggingService;
import com.dattran.job_finder_springboot.logging.entities.LogAction;
import com.dattran.job_finder_springboot.logging.entities.ObjectName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyService {
  CompanyRepository companyRepository;
  ObjectMapper objectMapper;
  LoggingService loggingService;
  BusinessStreamRepository businessStreamRepository;

  @NonFinal
  @Value("${app.tax-api}")
  String TAX_API;

  public Company addCompany(CompanyDto companyDto, HttpServletRequest httpServletRequest) {
    if (companyDto.getTax() == null
        || companyDto.getTax().isEmpty()
        || companyRepository.existsByTaxIdentificationNumber(companyDto.getTax())) {
      throw new AppException(ResponseStatus.INVALID_TAX);
    }
    Map<String, String> pathVariables = new HashMap<>();
    pathVariables.put("tax", companyDto.getTax());
    HttpResponse response =
        RequestUtil.sendRequest(
            HttpMethod.GET,
            TAX_API,
            new HashMap<>(),
            pathVariables,
            new HashMap<>(),
            new HashMap<>(),
            MediaType.APPLICATION_JSON);
    Company company = FnCommon.copyNonNullProperties(Company.class, companyDto);
    assert company != null;
    company.setTaxIdentificationNumber(companyDto.getTax());
    try {
      JsonNode jsonNode = objectMapper.readTree(response.getBody());
      JsonNode data = jsonNode.get("data");
      if (data.isNull()) {
        throw new AppException(ResponseStatus.TAX_NOT_FOUND);
      }
      JsonNode name = data.get("name");
      JsonNode internationalName = data.get("internationalName");
      JsonNode headQuarters = data.get("address");
      if (!name.isNull()) {
        company.setName(name.asText());
      }
      if (!internationalName.isNull()) {
        company.setInternationalName(internationalName.asText());
      }
      if (!headQuarters.isNull()) {
        company.setHeadQuarters(headQuarters.asText());
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Cannot read Json!", e);
    }
    List<BusinessStream> businessStreams = new ArrayList<>();
    for (Long businessCode : companyDto.getBusinessCodes()) {
      BusinessStream businessStream =
          businessStreamRepository
              .findByCode(businessCode)
              .orElseThrow(() -> new AppException(ResponseStatus.BUSINESS_STREAM_NOT_FOUND));
      businessStreams.add(businessStream);
    }
    company.setBusinessStreams(businessStreams);
    Company savedCompany = companyRepository.save(company);
    // Logging
    loggingService.writeLogEvent(
        savedCompany.getId(),
        LogAction.CREATE,
        HttpRequestUtil.getClientIp(httpServletRequest),
        ObjectName.COMPANY.name(),
        null,
        savedCompany);
    return savedCompany;
  }

  public Company getCompanyById(String id) {
    Company company = companyRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ResponseStatus.COMPANY_NOT_FOUND));
    if (!company.getIsDeleted()) return company;
    return null;
  }

  public void deleteById(String id, HttpServletRequest httpServletRequest) {
    Company company = getCompanyById(id);
    company.setIsDeleted(true);
    companyRepository.save(company);
  }

  public Company updateCompany(String id, @Valid UpdateCompanyDto updateCompanyDto, HttpServletRequest httpServletRequest) {
    Company company = getCompanyById(id);
    if (company == null) throw new AppException(ResponseStatus.COMPANY_NOT_FOUND);
    FnCommon.copyProperties(company, updateCompanyDto);
    List<BusinessStream> businessStreams = company.getBusinessStreams();
    businessStreams.clear();
    for (Long businessCode : updateCompanyDto.getBusinessCodes()) {
      BusinessStream businessStream =
              businessStreamRepository
                      .findByCode(businessCode)
                      .orElseThrow(() -> new AppException(ResponseStatus.BUSINESS_STREAM_NOT_FOUND));
      businessStreams.add(businessStream);
    }
    company.setBusinessStreams(businessStreams);
    Company savedCompany = companyRepository.save(company);
    // Logging
    loggingService.writeLogEvent(
            savedCompany.getId(),
            LogAction.UPDATE,
            HttpRequestUtil.getClientIp(httpServletRequest),
            ObjectName.COMPANY.name(),
            company,
            savedCompany);
    return company;
  }

  public Page<Company> getCompanies(String name, Pageable pageable) {
    if (Optional.ofNullable(name).isPresent()) {
      // Using elasticsearch
      return null;
    } else {
      return companyRepository.findAll(pageable);
    }
  }
}

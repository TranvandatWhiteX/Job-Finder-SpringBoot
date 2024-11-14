package com.dattran.job_finder_springboot.app.dtos;

import com.dattran.job_finder_springboot.domain.entities.Address;
import com.dattran.job_finder_springboot.domain.entities.Asset;
import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyDto {
    String name;

    String internationalName;

    @NotNull(message = "Tax must not be null")
    String tax;

    @NotNull(message = "Description must not be null")
    String description;

    @NotNull(message = "Website url must not be null")
    String websiteUrl;

    @NotNull(message = "Founded date must not be null")
    LocalDate foundedDate;

    @NotNull(message = "Number of employees must not be null")
    Long numberOfEmployees;

    String headQuarters;

    @Size(min = 1, message = "Address must not be null")
    List<Address> addresses;

    Asset asset;

    @Size(min = 1, message = "Business type must not be null")
    List<Long> businessCodes;
}

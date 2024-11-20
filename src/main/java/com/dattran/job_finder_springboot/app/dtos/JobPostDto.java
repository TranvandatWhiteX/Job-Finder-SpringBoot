package com.dattran.job_finder_springboot.app.dtos;

import com.dattran.job_finder_springboot.domain.entities.Address;
import com.dattran.job_finder_springboot.domain.entities.Salary;
import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobPostDto {
    @NotNull(message = "Expired date must not be null!")
    LocalDate expiredDate;

    @NotNull(message = "Job title must not be null!")
    String jobTitle;

    @NotNull(message = "Job requirement must not be null")
    String jobRequirement;

    @NotNull(message = "Job description must not be null")
    String jobDescription;

    @NotNull(message = "Responsibilities must not be null")
    String benefit;

    @NotNull(message = "Job level must not be null")
    List<JobLevel> jobLevels;

    @NotNull(message = "Job type must not be null")
    List<JobType> jobTypes;

    @NotNull(message = "Company id must not be null")
    String companyId;

    @NotNull(message = "Address must not be null")
    Address address;

    @Size(min = 1, message = "Skill codes must not be null")
    List<Long> skillCodes;

    @NotNull(message = "Salary must not be null")
    Salary salary;

    @NotNull(message = "Number requirement must not be null")
    Long numberRequirement;

    //    Todo: Experience = 0 => Not Required, Experience = 0.5 => Under 1 year, = 1 => 1 year,....
    @NotNull(message = "Experience must not be null")
    Long experience;

    @NotNull(message = "Business code must not be null")
    @Positive(message = "Business code must greater than 0")
    Long businessCode;

    @AssertTrue(message = "Expired date must be at least 14 days from today!")
    public boolean isExpiredDateValid() {
        return expiredDate != null && expiredDate.isAfter(LocalDate.now().plusDays(14));
    }
}

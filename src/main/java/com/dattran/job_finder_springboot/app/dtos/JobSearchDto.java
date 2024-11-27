package com.dattran.job_finder_springboot.app.dtos;

import com.dattran.job_finder_springboot.domain.enums.BusinessType;
import com.dattran.job_finder_springboot.domain.enums.JobLevel;
import com.dattran.job_finder_springboot.domain.enums.JobType;
import jakarta.validation.constraints.AssertTrue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobSearchDto {
  String keyword;

  Long provinceCode;

  BusinessType businessType;

  JobLevel jobLevel;

  JobType jobType;

  Long experience;

  Long minSalary;

  Long maxSalary;

  @AssertTrue(
      message =
          "Invalid search criteria! All fields cannot be null, and minSalary must be less than maxSalary.")
  public boolean isValid() {
    boolean allFieldsNotNull =
        keyword != null
            || provinceCode != null
            || businessType != null
            || jobLevel != null
            || jobType != null
            || experience != null
            || minSalary != null
            || maxSalary != null;
    boolean validSalaryRange = (minSalary == null || maxSalary == null) || (minSalary < maxSalary);
    return allFieldsNotNull && validSalaryRange;
  }
}

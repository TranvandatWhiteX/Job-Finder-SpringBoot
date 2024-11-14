package com.dattran.job_finder_springboot.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    String province;
    String district;
    String ward;
    String detail;
    Long provinceCode;
}

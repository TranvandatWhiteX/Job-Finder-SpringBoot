package com.dattran.job_finder_springboot.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date endDate;

    String state;

    Integer month;

    Integer year;

    String name;
}

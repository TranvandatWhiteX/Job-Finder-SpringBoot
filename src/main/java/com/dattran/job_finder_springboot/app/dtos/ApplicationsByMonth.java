package com.dattran.job_finder_springboot.app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicationsByMonth {
    private String month; // Dạng "YYYY-MM"
    private int totalApplications;
}

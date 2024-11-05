package com.dattran.job_finder_springboot.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatus {
    APPLIED("Đã ứng tuyển"),
    RECRUITER_VIEWED("Nhà tuyển dụng đã xem hồ sơ"),
    REJECTED("Hồ sơ chưa phù hợp"),
    RECRUITED_CONTACTED("Nhà tuển dụng đã liên lạc với ứng viên");
    final String description;
}

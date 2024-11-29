package com.dattran.job_finder_springboot.app.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatisticsController {
    // Todo: Thống kê số lượng job được tạo và số lượng đơn ứng tuyển
    //  theo tháng, năm, quý, theo ngành. Biểu đồ cột (2 cột).

    // Todo: Thống kê mức lương theo năm kinh nghiệm
}

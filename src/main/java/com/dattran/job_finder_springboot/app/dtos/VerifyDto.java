package com.dattran.job_finder_springboot.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyDto {
    @NotNull(message = "User id must be not null.")
    String userId;

    @NotNull(message = "Otp must be not null.")
    String otp;
}

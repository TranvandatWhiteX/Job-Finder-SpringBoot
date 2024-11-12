package com.dattran.job_finder_springboot.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {
    @NotNull
    private String email;

    @NotNull
    private String password;
}

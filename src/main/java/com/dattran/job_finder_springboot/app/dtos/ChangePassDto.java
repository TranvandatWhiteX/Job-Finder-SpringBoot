package com.dattran.job_finder_springboot.app.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassDto {
    @NotNull(message = "Old password must not be null!")
    String oldPass;

    @NotNull(message = "New password must not be null!")
    String newPass;
}

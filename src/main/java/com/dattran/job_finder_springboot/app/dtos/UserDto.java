package com.dattran.job_finder_springboot.app.dtos;

import com.dattran.job_finder_springboot.domain.entities.Address;
import com.dattran.job_finder_springboot.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull(message = "Full Name must not be null")
    String fullName;

    @Email(message = "Must be email")
    String email;

    @NotNull(message = "Password must not be null")
    String password;

    @NotNull(message = "Gender must not be null")
    Boolean gender;

    @NotNull(message = "Date of birth must not be null")
    LocalDate dateOfBirth;

    Set<UserRole> roles;

    String companyId;

    Address address;
}

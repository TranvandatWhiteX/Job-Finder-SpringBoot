package com.dattran.job_finder_springboot.logging.entities;

import com.dattran.job_finder_springboot.domain.entities.Address;
import com.dattran.job_finder_springboot.domain.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLog {
    String id;

    String fullName;

    Boolean gender;

    String email;

    LocalDate dateOfBirth;

    List<String> roles;

    Boolean isActive;

    Boolean isDeleted;

    UserState userState;

    Address address;
}

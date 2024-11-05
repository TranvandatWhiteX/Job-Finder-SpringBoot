package com.dattran.job_finder_springboot.domain.runners;

import com.dattran.job_finder_springboot.domain.entities.Role;
import com.dattran.job_finder_springboot.domain.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleRunner implements CommandLineRunner {
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            List<Role> roles = List.of(
                    Role.builder()
                            .name("USER")
                            .description("This is role for users. User can view jobs, apply jobs,...")
                            .build(),
                    Role.builder()
                            .name("ADMINISTRATOR")
                            .description("This is role for administrators. Admin can manage users, jobs,...")
                            .build(),
                    Role.builder()
                            .name("RECRUITER")
                            .description("This is role for recruiters. Recruiter can manage post jobs, review CVs,...")
                            .build()
            );
            log.info("Roles Added!");
            roleRepository.saveAll(roles);
        }
    }
}

package com.dattran.job_finder_springboot.domain.repositories;

import com.dattran.job_finder_springboot.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

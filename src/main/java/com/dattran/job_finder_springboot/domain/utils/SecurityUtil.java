package com.dattran.job_finder_springboot.domain.utils;

import com.dattran.job_finder_springboot.domain.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof User) {
                return (User) authentication.getPrincipal();
            }
            return null;
        }
        return null;
    }
}

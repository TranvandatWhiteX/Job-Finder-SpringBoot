package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.LoginDto;
import com.dattran.job_finder_springboot.app.responses.LoginResponse;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.enums.UserState;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    AuthenticationManager authenticationManager;

    public LoginResponse login(LoginDto loginDto) {
        User user = userRepository
                .findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AppException(ResponseStatus.USER_NOT_FOUND));
        if (!user.getIsActive() || user.getIsDeleted() || user.getUserState().equals(UserState.PENDING)) {
            throw new AppException(ResponseStatus.USER_ERROR);
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AppException(ResponseStatus.PASSWORD_NOT_MATCH);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword(), user.getAuthorities());
        authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        // Todo: Save token to DB
        // Todo: Save token to Redis
        return new LoginResponse(accessToken, refreshToken);
    }

    public void logout() {
    }

    public LoginResponse refreshToken(String refreshToken) {
        return null;
    }
}

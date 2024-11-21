package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.app.dtos.ChangePassDto;
import com.dattran.job_finder_springboot.app.dtos.UserDto;
import com.dattran.job_finder_springboot.app.dtos.VerifyDto;
import com.dattran.job_finder_springboot.app.responses.VerifyResponse;
import com.dattran.job_finder_springboot.domain.entities.Role;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.enums.UserRole;
import com.dattran.job_finder_springboot.domain.enums.UserState;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.repositories.RoleRepository;
import com.dattran.job_finder_springboot.domain.repositories.UserRepository;
import com.dattran.job_finder_springboot.domain.utils.FnCommon;
import com.dattran.job_finder_springboot.domain.utils.HttpRequestUtil;
import com.dattran.job_finder_springboot.logging.LoggingService;
import com.dattran.job_finder_springboot.logging.entities.LogAction;
import com.dattran.job_finder_springboot.logging.entities.ObjectName;
import com.dattran.job_finder_springboot.logging.entities.UserLog;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    OtpService otpService;
    RoleRepository roleRepository;
    BloomFilterService bloomFilterService;
    EmailService emailService;
    LoggingService loggingService;
    JwtService jwtService;

    public User createUser(UserDto userDto, HttpServletRequest httpServletRequest)  {
        boolean isEmailExisted = bloomFilterService.isEmailExisted(userDto.getEmail());
        if (isEmailExisted) {
            throw new AppException(ResponseStatus.EMAIL_ALREADY_EXISTED);
        }
        User user = FnCommon.copyNonNullProperties(User.class, userDto);
        assert user != null;
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserState(UserState.PENDING);
        List<Role> roles = new ArrayList<>();
        for (Long roleCode : userDto.getRoleCodes()) {
            Role role = roleRepository
                    .findByCode(roleCode)
                    .orElseThrow(() -> new AppException(ResponseStatus.ROLE_NOT_FOUND));
            roles.add(role);
        }
        // RECRUITER CODE: 1103L
        if (userDto.getCompanyId() != null && userDto.getRoleCodes().contains(1103L)) {
            user.setCompanyId(userDto.getCompanyId());
        }
        user.setRoles(roles);
        // Create OTP
        String otp = otpService.generateOTP(6);
        User savedUser = userRepository.save(user);
        otpService.storeOtp(savedUser.getId(), otp);
        // Add Email To BloomFilter
        bloomFilterService.addEmailToBloomFilter(userDto.getEmail());
        // Send Mail
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", otp);
        variables.put("username", savedUser.getFullName());
        emailService.sendEmail(
                savedUser.getEmail(),
                "OTP Verification",
                "send-otp.html",
                variables);
        // Logging
        UserLog userLog = FnCommon.copyNonNullProperties(UserLog.class, savedUser);
        assert userLog != null;
        userLog.setRoles(getRoles(savedUser.getRoles()));
        loggingService.writeLogEvent(savedUser.getId(), LogAction.CREATE, HttpRequestUtil.getClientIp(httpServletRequest), ObjectName.USER.name(), null, userLog);
        return savedUser;
    }

    private List<String> getRoles(List<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }

    public VerifyResponse verifyUser(VerifyDto verifyDto, Boolean isVerifyUser) {
        User user = getUserById(verifyDto.getUserId());
        if (isVerifyUser) {
            if (otpService.validateOtp(user.getId(), verifyDto.getOtp())) {
                user.setUserState(UserState.ACTIVE);
                user.setIsActive(true);
                userRepository.save(user);
                return VerifyResponse.builder()
                        .isVerified(true)
                        .message("OTP verified")
                        .build();
            } else {
                return handleVerifyFailed(verifyDto, user, user.getId());
            }
        } else {
            if (otpService.validateOtp(user.getId(), verifyDto.getOtp())) {
                String tempPass = user.getTempPassword();
                user.setPassword(tempPass);
                user.setTempPassword(null);
                userRepository.save(user);
                return VerifyResponse.builder()
                        .isVerified(true)
                        .message("OTP verified")
                        .build();
            } else {
                return handleVerifyFailed(verifyDto, user, user.getId());
            }
        }
    }

    private VerifyResponse handleVerifyFailed(VerifyDto verifyDTO, User user, String otpKey) {
        if (!otpService.isOtpExpired(verifyDTO.getUserId())) {
            return VerifyResponse.builder().isVerified(false).message("Wrong OTP").build();
        }
        otpService.deleteOtp(otpKey);
        String newOtp = otpService.generateOTP(6);
        otpService.storeOtp(otpKey, newOtp);
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", newOtp);
        variables.put("username", user.getUsername());
        emailService.sendEmail(
                user.getEmail(),
                "OTP Verification",
                "send-otp.html",
                variables);
        return VerifyResponse.builder()
                .isVerified(false)
                .message("OTP is expired! New OTP was sent to your email!")
                .build();
    }

    public User getUserById(String id, String token) {
        final String auth = token.substring(7);
        JWTClaimsSet claims = jwtService.getAllClaimsFromToken(auth);
        String email = (String) claims.getClaim("email");
        User user = getUserById(id);
        if (!user.getEmail().equals(email)) {
            throw new AppException(ResponseStatus.FORBIDDEN);
        }
        return user;
    }

    public void changePassword(String token, String id, ChangePassDto changePassDto) {
        User user = getUserById(id, token);
        boolean isMatch = passwordEncoder.matches(changePassDto.getOldPass(), user.getPassword());
        if (!isMatch) {
            throw new AppException(ResponseStatus.PASSWORD_NOT_MATCH);
        }
        String newPassword = passwordEncoder.encode(changePassDto.getNewPass());
        user.setTempPassword(newPassword);
        // Create OTP
        String otp = otpService.generateOTP(6);
        User savedUser = userRepository.save(user);
        otpService.storeOtp(savedUser.getId(), otp);
        // Send Mail
        Map<String, Object> variables = new HashMap<>();
        variables.put("otp", otp);
        variables.put("username", savedUser.getFullName());
        emailService.sendEmail(
                savedUser.getEmail(),
                "OTP Verification",
                "send-otp.html",
                variables);
    }

    private User getUserById(String id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ResponseStatus.USER_NOT_FOUND));
    }
}

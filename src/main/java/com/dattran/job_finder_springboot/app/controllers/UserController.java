package com.dattran.job_finder_springboot.app.controllers;

import com.dattran.job_finder_springboot.app.dtos.ChangePassDto;
import com.dattran.job_finder_springboot.app.dtos.UserDto;
import com.dattran.job_finder_springboot.app.dtos.UserFilterDto;
import com.dattran.job_finder_springboot.app.dtos.VerifyDto;
import com.dattran.job_finder_springboot.app.responses.VerifyResponse;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.services.UserService;
import com.dattran.job_finder_springboot.domain.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserDto userDto,
                                        HttpServletRequest httpServletRequest) {
        User user = userService.createUser(userDto, httpServletRequest);
        return ApiResponse.<User>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(user)
                .status(HttpStatus.CREATED)
                .message("User Created Successfully!")
                .build();
    }

    @PostMapping("/verify")
    public ApiResponse<Void> verifyUser(@RequestBody @Valid VerifyDto verifyDto,
                                        @RequestParam("ivu") Boolean isVerifyUser,
                                        HttpServletRequest httpServletRequest) {
        VerifyResponse verified = userService.verifyUser(verifyDto, isVerifyUser);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(verified.isVerified() ? HttpStatus.OK : HttpStatus.BAD_REQUEST)
                .message(verified.getMessage())
                .build();
    }

    @PostMapping("/change-password/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_RECRUITER')")
    public ApiResponse<Void> changePassword(@RequestHeader("Authorization") String token,
                                            @PathVariable String id,
                                            @RequestBody @Valid ChangePassDto changePassDto,
                                            HttpServletRequest httpServletRequest) {
        userService.changePassword(token, id, changePassDto);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .build();
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_RECRUITER')")
    public ApiResponse<User> getUserById(@PathVariable String id, HttpServletRequest httpServletRequest) {
        User user = userService.getUserById(id);
        return ApiResponse.<User>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(user)
                .status(HttpStatus.CREATED)
                .message("Get User Successfully!")
                .build();
    }

    @GetMapping
//    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public ApiResponse<List<User>> getAllUsers(HttpServletRequest httpServletRequest) {
        List<User> users = userService.getAllUsers();
        return ApiResponse.<List<User>>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(users)
                .status(HttpStatus.CREATED)
                .message("Get User Successfully!")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable String id, HttpServletRequest httpServletRequest) {
        User user = userService.updateUser(userDto, id, httpServletRequest);
        return ApiResponse.<User>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .result(user)
                .status(HttpStatus.OK)
                .message("Update Created Successfully!")
                .build();
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_RECRUITER')")
    public ApiResponse<Void> deleteUser(@PathVariable String id, HttpServletRequest httpServletRequest) {
        userService.deleteUser(id, httpServletRequest);
        return ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now().toString())
                .path(httpServletRequest.getRequestURI())
                .requestMethod(httpServletRequest.getMethod())
                .status(HttpStatus.CREATED)
                .message("Get User Successfully!")
                .build();
    }
}

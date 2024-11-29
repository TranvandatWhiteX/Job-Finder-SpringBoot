package com.dattran.job_finder_springboot.domain.services;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpService {
    RedisTemplate<String, Object> redisTemplate;

    @NonFinal
    @Value("${otp.expiration}")
    long OTP_EXPIRATION_MINUTES;

    public String generateOTP(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public void storeOtp(String userId, String otpCode) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(userId, otpCode, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);
    }

    public boolean isOtpExpired(String userId) {
        Long expireTime = redisTemplate.getExpire(userId, TimeUnit.SECONDS);
        return expireTime <= 0;
    }

    public String getOtp(String userId) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return (String) ops.get(userId);
    }

    public void deleteOtp(String userId) {
        redisTemplate.delete(userId);
    }

    public boolean validateOtp(String userId, String otpCode) {
        String storedOtp = getOtp(userId);
        if (storedOtp != null && storedOtp.equals(otpCode)) {
            deleteOtp(userId);
            return true;
        }
        return false;
    }
}

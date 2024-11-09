package com.dattran.job_finder_springboot.config;

import com.dattran.job_finder_springboot.domain.entities.User;
import com.dattran.job_finder_springboot.domain.enums.ResponseStatus;
import com.dattran.job_finder_springboot.domain.exceptions.AppException;
import com.dattran.job_finder_springboot.domain.services.JwtService;
import com.dattran.job_finder_springboot.domain.utils.SecurityUtil;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtDecoder implements JwtDecoder {
    JwtService jwtService;
    SecurityUtil securityUtil;

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Value("${jwt.secret-key}")
    @NonFinal
    String ACCESS_KEY;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            User user = securityUtil.getCurrentUser();
            try{
                boolean isVerified = jwtService.verifyToken(token, user, false);
                if(!isVerified) {
                    throw new AppException(ResponseStatus.INVALID_TOKEN);
                }
            }  catch (JOSEException e) {
                throw new AppException(ResponseStatus.JWT_ERROR);
            }
            if (Objects.isNull(nimbusJwtDecoder)) {
                SecretKeySpec secretKeySpec = new SecretKeySpec(ACCESS_KEY.getBytes(), "HS512");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }
            return nimbusJwtDecoder.decode(token);
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}

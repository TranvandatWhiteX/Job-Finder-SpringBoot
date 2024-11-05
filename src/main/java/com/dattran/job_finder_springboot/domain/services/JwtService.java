package com.dattran.job_finder_springboot.domain.services;

import com.dattran.job_finder_springboot.domain.entities.Role;
import com.dattran.job_finder_springboot.domain.entities.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class JwtService {
  @Value("${jwt.expiration}")
  @NonFinal
  long EXPIRATION_AC_KEY;

  @Value("${jwt.secret-key}")
  @NonFinal
  String ACCESS_KEY;

  @Value("${jwt.refresh-key}")
  @NonFinal
  String REFRESH_KEY;

  @Value("${jwt.expiration-rf-key}")
  @NonFinal
  long EXPIRATION_RF_KEY;

  public String generateRefreshToken(User user) {
    return generateToken(user, Optional.empty(), EXPIRATION_RF_KEY, REFRESH_KEY);
  }

  public String generateAccessToken(User user) {
    List<String> roles = user.getRoles().stream().map(Role::getName).toList();
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", roles);
    claims.put("email", user.getEmail());
    return generateToken(user, Optional.of(claims), EXPIRATION_AC_KEY, ACCESS_KEY);
  }

  public String generateToken(UserDetails userDetails, Optional<Map<String, Object>> claims, long expiration, String key) {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS384);
    JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
            .subject(userDetails.getUsername())
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(expiration, ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString());
    // Additional claims
    claims.ifPresent(stringObjectMap -> stringObjectMap.forEach(claimsBuilder::claim));
    JWTClaimsSet jwtClaimsSet = claimsBuilder.build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);
    try {
      jwsObject.sign(new MACSigner(key.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Cannot create token", e);
      throw new RuntimeException(e);
    }
  }

  public JWTClaimsSet getAllClaimsFromToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      return signedJWT.getJWTClaimsSet();
    } catch (ParseException e) {
      log.error("Cannot extract token", e);
      throw new RuntimeException(e);
    }
  }

  public boolean verifyToken(String token, UserDetails userDetails, Boolean isRefreshToken) throws JOSEException, ParseException {
    JWSVerifier verifier;
    if (isRefreshToken) {
      verifier = new MACVerifier(REFRESH_KEY.getBytes());
    } else {
      verifier = new MACVerifier(ACCESS_KEY.getBytes());
    }
    SignedJWT signedJWT = SignedJWT.parse(token);
    var verified = signedJWT.verify(verifier);
    String username = signedJWT.getJWTClaimsSet().getSubject();
    return username.equals(userDetails.getUsername()) && verified && !isTokenExpired(token);
  }

  public boolean isTokenExpired(String token) throws ParseException {
    SignedJWT signedJWT = SignedJWT.parse(token);
    JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
    Date expirationTime = claims.getExpirationTime();
    return expirationTime.before(new Date());
  }
}

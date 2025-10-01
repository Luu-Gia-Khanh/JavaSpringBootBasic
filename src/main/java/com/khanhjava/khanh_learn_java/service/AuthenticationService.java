package com.khanhjava.khanh_learn_java.service;

import com.khanhjava.khanh_learn_java.dto.request.IntrospectRequest;
import com.khanhjava.khanh_learn_java.dto.request.LoginRequest;
import com.khanhjava.khanh_learn_java.dto.response.AuthResponse;
import com.khanhjava.khanh_learn_java.dto.response.IntrospectResponse;
import com.khanhjava.khanh_learn_java.entity.User;
import com.khanhjava.khanh_learn_java.exception.AppException;
import com.khanhjava.khanh_learn_java.exception.ErrorCode;
import com.khanhjava.khanh_learn_java.mapper.UserMapper;
import com.khanhjava.khanh_learn_java.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    @NonFinal // ! Not inject to constructor
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                                  .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean matchPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matchPassword) {
            throw new AppException(ErrorCode.PASSWORD_VALID);
        }

        String token = generateToken(user);

        return AuthResponse.builder().accessToken(token).authenticated(true).build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return IntrospectResponse.builder()
                                 .valid(signedJWT.verify(jwsVerifier) && expirationTime.after(new Date()))
                                 .build();

    }

    String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new JWTClaimsSet
                .Builder().subject(user.getUsername())
                          .issuer("khanhjava.com")
                          .issueTime(new Date())
                          .expirationTime(
                                  new Date(
                                          Instant.now()
                                                 .plus(1, ChronoUnit.HOURS)
                                                 .toEpochMilli()
                                  )
                          ).claim("scope", buildScope(user))
                          .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }
}

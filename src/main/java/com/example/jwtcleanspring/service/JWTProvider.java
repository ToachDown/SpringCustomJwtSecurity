package com.example.jwtcleanspring.service;

import com.example.jwtcleanspring.exception.exceptions.JwtHandlerException;
import com.example.jwtcleanspring.model.JWT;
import com.example.jwtcleanspring.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Service
public class JWTProvider {

    private final String secret;
    private final ObjectMapper objectMapper;

    public JWTProvider(@Value("secret.key") String secret,
                       ObjectMapper objectMapper) {
        this.secret = secret;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    private String calculateSignature(final JWT jwt) {
        if(jwt.getPayload() == null || jwt.getHeader() == null) {
            throw new JwtHandlerException("not enough data for generate token signature");
        }
        final String headerEncode = Base64Utils.encodeToString(objectMapper.writeValueAsString(jwt.getHeader()).getBytes());
        final String payloadEncode = Base64Utils.encodeToString(objectMapper.writeValueAsString(jwt.getPayload()).getBytes());
        final SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return HexUtils.toHexString(mac.doFinal((headerEncode + "." + payloadEncode).getBytes()));
    }

    @SneakyThrows
    public String generateToken (final JWT jwt) {
        final String jwtHeaderEncode = Base64Utils.encodeToString(objectMapper.writeValueAsString(jwt.getHeader()).getBytes());
        final String jwtPayloadEncode = Base64Utils.encodeToString(objectMapper.writeValueAsString(jwt.getPayload()).getBytes());
        final String jwtSignature = calculateSignature(jwt);
        return jwtHeaderEncode + "." + jwtPayloadEncode + "." + jwtSignature;
    }

    public JWT extractData(final User user) {
        return JWT.builder()
                .header(JWT.JWTHeader.builder()
                        .algorithm("SHA-256")
                        .type("JWT")
                        .build())
                .payload(JWT.JWTPayload.builder()
                        .created(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                        .password(user.getPassword())
                        .username(user.getUsername())
                        .role(user.getRole())
                        .expired(LocalDateTime.now().plusHours(2).atZone(ZoneId.systemDefault()).toInstant().getEpochSecond())
                        .build())
                .build();
    }

    @SneakyThrows
    public boolean verifyToken(final User user,
                               final String token) {
        if (token == null) {
            return false;
        }
        String payloadJson = token.split("\\.")[1];
        JWT.JWTPayload payload = objectMapper.readValue(Base64Utils.decodeFromString(payloadJson), JWT.JWTPayload.class);
        if (!payload.getPassword().equals(user.getPassword())
         || !payload.getRole().equals(user.getRole())
         || !payload.getUsername().equals(user.getUsername())
         ||  payload.getExpired() < LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond()) {
            return false;
        }
        JWT jwt = extractData(user);
        jwt.setPayload(payload);
        String verifyToken = generateToken(jwt);
        return verifyToken.equals(token);
    }

    @SneakyThrows
    public JWT encodeToken (final String token) {
        String[] parts = token.split("\\.");
        return JWT.builder()
                .header(objectMapper.readValue(Base64Utils.decodeFromString(parts[0]), JWT.JWTHeader.class))
                .payload(objectMapper.readValue(Base64Utils.decodeFromString(parts[1]), JWT.JWTPayload.class))
                .build();
    }
}

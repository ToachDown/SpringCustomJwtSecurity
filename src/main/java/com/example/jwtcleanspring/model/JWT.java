package com.example.jwtcleanspring.model;

import com.example.jwtcleanspring.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class JWT {

    private JWTHeader header;
    private JWTPayload payload;

    @Data
    @AllArgsConstructor
    @Builder
    public static class JWTHeader {

        private String algorithm;
        private String type;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class JWTPayload {

        private String username;
        private Role role;
        private String password;
        private long created;
        private long expired;
    }

}

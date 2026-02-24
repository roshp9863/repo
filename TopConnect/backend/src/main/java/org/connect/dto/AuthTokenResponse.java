package org.connect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn; // in seconds
    private UserDTO user;
}


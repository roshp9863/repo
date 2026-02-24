package org.connect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSignupRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ARTIST|USER", message = "Role must be ARTIST or USER")
    private String role;

    @JsonProperty("displayName")
    private String displayName; // Required for ARTIST role

    // ── Artist verification proof (required for ARTIST signup) ──
    private String instagramUrl;
    private String youtubeUrl;
    private String certificateInfo;  // description of certifications, degrees, etc.
}

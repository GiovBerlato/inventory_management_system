package io.github.giovberlato.inventory_management_system.service;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 3600L; // 1 hour

        String scopes = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
               .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();
        
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

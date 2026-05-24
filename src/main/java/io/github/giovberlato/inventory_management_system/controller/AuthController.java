package io.github.giovberlato.inventory_management_system.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import io.github.giovberlato.inventory_management_system.contract.LoginRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.TokenResponseDTO;
import io.github.giovberlato.inventory_management_system.service.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ims/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO auth(@RequestBody @Valid LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = jwtService.generateToken(authentication);
        return new TokenResponseDTO(token);
    }
}

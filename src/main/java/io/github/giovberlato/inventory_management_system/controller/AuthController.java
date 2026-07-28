package io.github.giovberlato.inventory_management_system.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import io.github.giovberlato.inventory_management_system.contract.LoginRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.RegisterRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.TokenResponseDTO;
import io.github.giovberlato.inventory_management_system.model.User;
import io.github.giovberlato.inventory_management_system.repository.UserRepository;
import io.github.giovberlato.inventory_management_system.security.Role;
import io.github.giovberlato.inventory_management_system.service.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ims/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDTO auth(@RequestBody @Valid LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = jwtService.generateToken(authentication);
        return new TokenResponseDTO(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody @Valid RegisterRequestDTO registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken");
        }

        //Hash the password & assign default ROLE_USER
        User newUser = new User(
            registerRequest.getUsername(),
            passwordEncoder.encode(registerRequest.getPassword()),
            Role.ROLE_USER
        );
        userRepository.save(newUser);

        return "User registered successfully! Proceed to login at \"/ims/auth/login\".";
    }
}

package io.github.giovberlato.inventory_management_system;

import io.github.giovberlato.inventory_management_system.security.SecurityConfig;
import io.github.giovberlato.inventory_management_system.contract.LoginRequestDTO;
import io.github.giovberlato.inventory_management_system.contract.RegisterRequestDTO;
import io.github.giovberlato.inventory_management_system.controller.AuthController;
import io.github.giovberlato.inventory_management_system.security.Role;
import io.github.giovberlato.inventory_management_system.model.User;
import io.github.giovberlato.inventory_management_system.repository.UserRepository;
import io.github.giovberlato.inventory_management_system.service.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTests {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("password123");

        Authentication authentication = mock(Authentication.class);

        given(authenticationManager.authenticate(any(Authentication.class)))
                .willReturn(authentication);

        given(jwtService.generateToken(authentication))
                .willReturn("fake-jwt-token");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        verify(authenticationManager)
                .authenticate(any(Authentication.class));

        verify(jwtService)
                .generateToken(authentication);
    }

    @Test
    void login_ShouldAuthenticateUsingProvidedCredentials() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("password123");

        Authentication authentication = mock(Authentication.class);

        given(authenticationManager.authenticate(any(Authentication.class)))
                .willReturn(authentication);

        given(jwtService.generateToken(authentication))
                .willReturn("fake-jwt-token");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatusOk();

        ArgumentCaptor<Authentication> captor =
                ArgumentCaptor.forClass(Authentication.class);

        verify(authenticationManager)
                .authenticate(captor.capture());

        Authentication submittedAuthentication = captor.getValue();

        assertThat(submittedAuthentication.getName())
                .isEqualTo("giovanni");

        assertThat(submittedAuthentication.getCredentials())
                .isEqualTo("password123");
    }

    @Test
    void login_ShouldReturn400_WhenUsernameIsBlank() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("");
        request.setPassword("password123");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_ShouldReturn400_WhenPasswordIsBlank() throws Exception {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_ShouldReturn201_WhenUsernameIsAvailable() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("password123");

        given(userRepository.findByUsername("giovanni"))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode("password123"))
                .willReturn("hashed-password");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        verify(userRepository)
                .findByUsername("giovanni");

        verify(passwordEncoder)
                .encode("password123");

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void register_ShouldCreateUserWithEncodedPasswordAndUserRole()
            throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("password123");

        given(userRepository.findByUsername("giovanni"))
                .willReturn(Optional.empty());

        given(passwordEncoder.encode("password123"))
                .willReturn("hashed-password");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.CREATED);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(captor.capture());

        User savedUser = captor.getValue();

        assertThat(savedUser.getUsername())
                .isEqualTo("giovanni");

        assertThat(savedUser.getPassword())
                .isEqualTo("hashed-password");

        assertThat(savedUser.getRole())
                .isEqualTo(Role.ROLE_USER);
    }

    @Test
    void register_ShouldReturn400_WhenUsernameAlreadyExists()
            throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("password123");

        User existingUser = new User(
                "giovanni",
                "existing-hash",
                Role.ROLE_USER
        );

        given(userRepository.findByUsername("giovanni"))
                .willReturn(Optional.of(existingUser));

        assertThat(
                mvc.post()
                        .uri("/ims/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verify(userRepository)
                .findByUsername("giovanni");

        verify(passwordEncoder, never())
                .encode(anyString());

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void register_ShouldReturn400_WhenUsernameIsBlank()
            throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("");
        request.setPassword("password123");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void register_ShouldReturn400_WhenPasswordIsBlank()
            throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("giovanni");
        request.setPassword("");

        assertThat(
                mvc.post()
                        .uri("/ims/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).hasStatus(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }
}
package com.rigel.balanca.auth;

import com.rigel.balanca.user.User;
import com.rigel.balanca.user.UserRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(com.rigel.balanca.config.SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private com.rigel.balanca.user.AppUserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void loginShouldReturnAuthenticatedUser() throws Exception {
        User user = new User();
        user.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        user.setUsername("admin");
        user.setPasswordHash("$2a$10$QXYR0WQnWwH1nQ1BM8DT6emQfLFmYyqS/fWznuaCG2lLBVmWHAXoO");
        user.setCreatedAt(OffsetDateTime.now());
        user.setUpdatedAt(OffsetDateTime.now());
        user.setActive(true);

        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User("admin", user.getPasswordHash(), java.util.List.of());

        when(userDetailsService.loadUserByUsername("admin")).thenReturn(securityUser);
        when(passwordEncoder.matches("admin123", user.getPasswordHash())).thenReturn(true);
        when(userRepository.findByUsernameAndActiveTrue(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin","password":"admin123"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("admin"));
    }
}

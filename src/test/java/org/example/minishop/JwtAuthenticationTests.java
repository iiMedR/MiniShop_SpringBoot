package org.example.minishop;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.minishop.model.User;
import org.example.minishop.repository.UserRepository;
import org.example.minishop.service.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtAuthenticationTests.ProtectedEndpoint.class)
class JwtAuthenticationTests {
    private static final String EMAIL = "security-test@example.com";

    @Autowired MockMvc mvc;
    @Autowired JwtService jwtService;
    @MockitoBean UserRepository userRepository;
    @Value("${jwt.secret}") String secret;

    @BeforeEach
    void setUpUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword("unused-in-bearer-authentication");
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
    }

    @Test
    void validTokenAuthenticatesItsOwner() throws Exception {
        mvc.perform(get("/test/protected").header("Authorization", "Bearer " + jwtService.generateToken(EMAIL)))
                .andExpect(status().isOk())
                .andExpect(content().string(EMAIL));
    }

    @Test
    void missingTokenReturnsUnauthorized() throws Exception {
        mvc.perform(get("/test/protected"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void malformedTokenReturnsUnauthorized() throws Exception {
        assertUnauthorized("not-a-jwt");
    }

    @Test
    void expiredTokenReturnsUnauthorized() throws Exception {
        String token = Jwts.builder().subject(EMAIL)
                .expiration(new Date(System.currentTimeMillis() - 60_000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))).compact();
        assertUnauthorized(token);
    }

    @Test
    void incorrectSignatureReturnsUnauthorized() throws Exception {
        String token = Jwts.builder().subject(EMAIL)
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Jwts.SIG.HS256.key().build()).compact();
        assertUnauthorized(token);
    }

    @Test
    void tokenForDeletedUserReturnsUnauthorized() throws Exception {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        assertUnauthorized(jwtService.generateToken(EMAIL));
    }

    private void assertUnauthorized(String token) throws Exception {
        mvc.perform(get("/test/protected").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @RestController
    static class ProtectedEndpoint {
        @GetMapping("/test/protected")
        String owner(Principal principal) {
            return principal.getName();
        }
    }
}

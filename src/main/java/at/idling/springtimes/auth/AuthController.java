package at.idling.springtimes.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final String expectedUsername;
    private final String expectedPassword;

    public AuthController(
            JwtUtil jwtUtil,
            @Value("${app.auth.username}") String expectedUsername,
            @Value("${app.auth.password}") String expectedPassword
    ) {
        this.jwtUtil = jwtUtil;
        this.expectedUsername = expectedUsername;
        this.expectedPassword = expectedPassword;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> token(@Valid @RequestBody AuthRequest request) {
        if (!request.username().equals(expectedUsername) || !request.password().equals(expectedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new AuthResponse(jwtUtil.generate()));
    }
}

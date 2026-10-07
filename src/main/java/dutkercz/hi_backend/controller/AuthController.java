package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.LoginRequestDto;
import dutkercz.hi_backend.dto.LoginResponse;
import dutkercz.hi_backend.exceptions.CookieInvalidException;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequestDto request,
                                               HttpServletResponse response) {
        var authToken = new UsernamePasswordAuthenticationToken(request.username(),
                                                                request.password());
        var userAuth = authenticationManager.authenticate(authToken);

        var refreshToken = jwtService.generateRefreshToken((User) userAuth.getPrincipal());
        var accessToken = jwtService.generateAccessToken((User) userAuth.getPrincipal());

        jwtService.setCookie(refreshToken, response);

        return ResponseEntity.ok(new LoginResponse(accessToken));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "refreshToken") String refreshToken,
                                          HttpServletResponse response) throws NoSuchAlgorithmException {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CookieInvalidException("refreshToken cannot be null or blank");
        }
        return ResponseEntity.ok(jwtService.renewRefreshToken(refreshToken, response));
    }


}

package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.LoginResponse;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    private static final String SECRET_KEY = "batman-superman-flash-lanterna-HASH-256b";
    private static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofDays(1);
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse renewRefreshToken(String receivedRefresh,
                                    HttpServletResponse response) throws NoSuchAlgorithmException {
        String receivedHashedToken = hashRefreshToken(receivedRefresh);
        var claims = validateRefreshToken(receivedRefresh);
        var user = (User) userRepository.findByEmail(claims.getSubject());

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if(!receivedHashedToken.equals(user.getRefreshToken())){
            throw new JwtException("Refresh token invalido");
        }
        var newRefreshToken = generateRefreshToken(user);
        setCookie(newRefreshToken, response);
        return new LoginResponse( generateAccessToken(user));
    }

    //Gerar tokens :::::::::::::::
    @Transactional
    public String generateRefreshToken(User user) {
        try {
            var issuedAt = new Date();
            var refreshToken = Jwts.builder()
                .issuedAt(issuedAt)
                .expiration(Date.from(issuedAt.toInstant().plus(REFRESH_TOKEN_VALIDITY)))
                .claim("type", "refresh")
                .subject(user.getEmail())
                .signWith(getSingKey())
                .compact();

            user.setRefreshToken(hashRefreshToken(refreshToken));
            return refreshToken;
        }catch (NoSuchAlgorithmException | WeakKeyException e){
            throw new WeakKeyException("Erro ao gerar token");
        }
    }

    public void setCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(Math.toIntExact(REFRESH_TOKEN_VALIDITY.toSeconds()));
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public String generateAccessToken(User user) {
        try {
            return Jwts.builder()
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + 60000))
                   .claim("type", "access")
                   .subject(user.getEmail())
                   .signWith(getSingKey())
                   .compact();
        }catch (WeakKeyException e){
            throw new WeakKeyException("Erro ao gerar token");
        }
    }
    //::::::::::::::::::

    public String getSubject(String token) {
        try {
            var claims = Jwts.parser()
                             .verifyWith(getSingKey())
                             .build()
                             .parseSignedClaims(token)
                             .getPayload();
            if(!"access".equals(claims.get("type"))){
                throw new JwtException("Token inválido");
            }
            return claims.getSubject();
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }

    private Claims validateRefreshToken(String token){
        try {
            var claims = Jwts.parser()
                             .verifyWith(getSingKey())
                             .build()
                             .parseSignedClaims(token)
                             .getPayload();

            if (!"refresh".equals(claims.get("type", String.class))){
                throw new JwtException("Refresh token inválido");
            }
            return claims;
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }
    }

    private SecretKey getSingKey() {
        byte[] encodedKey = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(encodedKey);
    }

    private String hashRefreshToken(String refreshToken) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        var hash = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);

    }
}

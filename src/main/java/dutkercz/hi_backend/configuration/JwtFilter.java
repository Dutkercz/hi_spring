package dutkercz.hi_backend.configuration;

import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.repository.UserRepository;
import dutkercz.hi_backend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            String emailSubject = jwtService.getSubject(token);

            if (emailSubject != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                var user = userRepository.findByEmail(emailSubject)
                                         .orElseThrow(() ->
                                                              new ResourceNotFoundException("Usuário não encontrado"));

                var authUser = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authUser);
            }
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}

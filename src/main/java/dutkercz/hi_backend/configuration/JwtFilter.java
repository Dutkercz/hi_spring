package dutkercz.hi_backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dutkercz.hi_backend.repository.UserRepository;
import dutkercz.hi_backend.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

       // try {
            String header = request.getHeader("Authorization");
            if (header == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            String emailSubject = jwtService.getSubject(token);

            if (emailSubject != null) {
                var user = userRepository.findByEmail(emailSubject);
                var authUser = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
                var context = SecurityContextHolder.getContext();
                if (context.getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authUser);
                }
            }
            filterChain.doFilter(request, response);
        //}catch (JwtException e){
//            System.out.println("Invalid token on filter chain");
//           throw new JwtException(e.getMessage());
        //}
    }
}

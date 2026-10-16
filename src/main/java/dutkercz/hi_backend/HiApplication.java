package dutkercz.hi_backend;

import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.UserRole;
import dutkercz.hi_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class HiApplication implements CommandLineRunner {

    @Value("${credentials.admin.username}")
    private String ADMIN_USERNAME;

    @Value("${credentials.admin.password}")
    private String ADMIN_PASSWORD;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            var user = new User();
            user.setEmail(ADMIN_USERNAME);
            user.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            user.setFullName("Cristian Tiago Dutkercz Rosa");
            user.setRole(UserRole.ADMIN);
            userRepository.save(user);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HiApplication.class, args);
    }

}

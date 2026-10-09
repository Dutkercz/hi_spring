package dutkercz.hi_backend.factory;

import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserFactory {

    public static User createUser(PasswordEncoder encoder) {
        return  new User(null, "cris","string", "auth-test@email.com",
                         encoder.encode("StrongPass123"), true, LocalDateTime.now(),
                         null, UserRole.ADMIN, null);
    }
}

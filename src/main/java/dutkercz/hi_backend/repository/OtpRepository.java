package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp, Long> {
}

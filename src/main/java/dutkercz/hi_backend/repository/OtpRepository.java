package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByOtpHash(String otpHash);
}

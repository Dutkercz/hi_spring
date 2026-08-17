package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRepository extends JpaRepository<Stay, Long> {
}

package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findAllByCheckOutBetween(LocalDateTime firstDay, LocalDateTime lastDay);

    List<Stay> findAllByCheckInBetween(LocalDateTime firstDay, LocalDateTime lastDay);
}

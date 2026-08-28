package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.DailyPrices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyPriceRepository extends JpaRepository<DailyPrices, Long> {
}

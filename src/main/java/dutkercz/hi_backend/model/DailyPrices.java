package dutkercz.hi_backend.model;

import dutkercz.hi_backend.dto.DailyPricesDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyPrices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal oneGuestPrice;
    private BigDecimal twoGuestPrice;
    private BigDecimal threeGuestPrice;
    private BigDecimal fourGuestPrice;

}

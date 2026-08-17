package dutkercz.hi_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_extras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Extras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer sodaQuantity;
    private Integer waterQuantity;
    private Integer coffeeQuantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalExtraPrice;
}

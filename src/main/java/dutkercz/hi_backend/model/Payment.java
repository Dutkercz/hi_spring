package dutkercz.hi_backend.model;

import dutkercz.hi_backend.model.enums.PaymentMethod;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_payments")
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Stay stay;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private LocalDate creationDate;
}

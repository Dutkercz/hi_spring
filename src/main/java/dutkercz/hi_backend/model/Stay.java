package dutkercz.hi_backend.model;

import dutkercz.hi_backend.model.enums.StayStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_stays")
@ToString
public class Stay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Room room;

    private BigDecimal dailyPrice;
    private BigDecimal paidPrice;
    private BigDecimal totalPrice;
    private Boolean isPaid;

    private Integer totalGuests;
    private Long dailyRates;

    @Enumerated(EnumType.STRING)
    private StayStatus stayStatus = StayStatus.CURRENT;

    @Column(nullable = false, updatable = false)
    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stay")
    private List<StayGuest> stayGuests = new ArrayList<>();

    public void addStayGuest(StayGuest stayGuest) {
        stayGuests.add(stayGuest);
        stayGuest.setStay(this);
    }

    public void removeStayGuest(StayGuest stayGuest) {
        stayGuests.remove(stayGuest);
        stayGuest.setStay(null);
    }

    public void addPaymentAmount(BigDecimal amount) {
        this.paidPrice = this.paidPrice.add(amount);
    }
}

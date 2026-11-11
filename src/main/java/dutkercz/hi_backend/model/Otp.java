package dutkercz.hi_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Entity
@Table(name = "tb_otps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    private String otpHash;
    private Boolean isValid;
    private Instant createdAt;

    public Otp(User user, String otpHash) {
        init();
        this.user = user;
        this.otpHash = otpHash;
    }

    private void init(){
        this.createdAt = Instant.now();
        this.isValid = true;
    }
}

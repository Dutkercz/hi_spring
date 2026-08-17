package dutkercz.hi_backend.model;

import dutkercz.hi_backend.model.enums.ClientStatusEnum;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_clients")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idClient;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatusEnum status;

    @OneToMany(mappedBy = "client", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Address> addresses;

    @Column(nullable = false, updatable = false)
    @Timestamp
    private LocalDateTime createdAt;


    public Client(String firstName, String lastName, String cpf, String cnpj, String phoneNumber,
                  List<Address> addresses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
        init();
    }

    public Client() {
        init();
    }

    private void init() {
        this.idClient = UUID.randomUUID().toString();
        this.status = ClientStatusEnum.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setClient(this);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setClient(null);
    }
}

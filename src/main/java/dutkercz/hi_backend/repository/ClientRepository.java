package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(String cpf);

    List<Client> findTop10ByCnpj(String cnpj);

    List<Client> findTop10ByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}

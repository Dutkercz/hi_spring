package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

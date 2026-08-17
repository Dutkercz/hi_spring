package dutkercz.hi_backend.repository;

import dutkercz.hi_backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

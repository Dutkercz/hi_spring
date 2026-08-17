package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.mapper.RoomMapper;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomForCardDto> getAllRooms() {

        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(r -> {
            Stay lastStay = null;
            if (r.getStays() != null && !r.getStays().isEmpty()) {
                Stay possibleLast = r.getStays().getLast();
                if (possibleLast.getStayStatus() == StayStatus.CURRENT) {
                    lastStay = possibleLast;
                }
            }
            return roomMapper.toRoomForCardDto(r, lastStay);
        }).toList();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }
}

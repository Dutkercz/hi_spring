package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.mapper.RoomMapper;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final StayService stayService;

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

    @Transactional
    public RoomResponseDto addDaily(Long id) {
        var room = getRoomById(id);
        var stay = room.getStays().stream().filter(s -> s.getStayStatus() == StayStatus.CURRENT).findFirst()
                       .orElseThrow(() -> new EntityNotFoundException("No stays actives for this room"));
        stayService.addStay(stay.getId());
        return roomMapper.toResponseRoomDto(room);
    }
}

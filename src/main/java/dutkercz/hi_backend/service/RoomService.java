package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.dto.room.RoomUpdateDto;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.exceptions.RoomBadRequestException;
import dutkercz.hi_backend.mapper.RoomMapper;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.RoomStatusEnum;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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
        }).sorted(Comparator.comparing(RoomForCardDto::id)).toList();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                     .orElseThrow(() -> new ResourceNotFoundException("Apartamento não encontrado"));
    }

    @Transactional
    public RoomResponseDto updateRoomConfig(Long id, RoomUpdateDto updateDto) {
        var room = getRoomById(id);
        room.setDoubleBeds(updateDto.doubleBeds() != null && updateDto.doubleBeds() >= 0 ?
                           updateDto.doubleBeds() : room.getDoubleBeds());
        room.setSingleBeds(updateDto.singleBeds() != null && updateDto.singleBeds() >= 0 ?
                           updateDto.singleBeds() : room.getSingleBeds());

        return  roomMapper.toResponseRoomDto(room);
    }

    public RoomResponseDto addRoom() {
        var rooms = roomRepository.findAll().stream().sorted(
                Comparator.comparingInt(
                        r -> Integer.parseInt(r.getRoomNumber()))).toList();

        int newRoomNumber = rooms.size()+1;
        int roomIndex = 0;

        for(int i = 1; i <= rooms.size(); i++) {
            if (Integer.parseInt(rooms.get(roomIndex).getRoomNumber()) != i){
                newRoomNumber = i;
                break;
            }
            roomIndex++;
        }

        Room room = new Room(null, String.valueOf(newRoomNumber), 0, 0,
                             RoomStatusEnum.AVAILABLE, new ArrayList<>());
        return  roomMapper.toResponseRoomDto(roomRepository.save(room));
    }

    public void deleteRoom(Long id) {
        var room = getRoomById(id);
        if (room.getStatus().cantDeleteRoom()) {
            throw new RoomBadRequestException("O apartamento possui uma hospedagem ativa.");
        }
        roomRepository.delete(room);
    }
}

package dutkercz.hi_backend.service.validations.room;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomValidation {

    private final RoomRepository roomRepository;
    private final List<RoomValidations> roomValidations;

    public Room validateRoomInRequest(StayRequestDto requestDto){
        Room room = roomRepository.findById(requestDto.roomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        roomValidations.forEach(v -> v.validateRoom(requestDto, room));
        return room;
    }
}

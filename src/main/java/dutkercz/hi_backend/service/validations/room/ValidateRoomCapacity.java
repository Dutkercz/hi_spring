package dutkercz.hi_backend.service.validations.room;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.exceptions.BusinessException;
import dutkercz.hi_backend.model.Room;
import org.springframework.stereotype.Component;

@Component
public class ValidateRoomCapacity implements RoomValidations {
    @Override
    public void validateRoom(StayRequestDto requestDto, Room room) {
        int totalCapacity = 2 * room.getDoubleBeds() + room.getSingleBeds();
        int totalGuest = requestDto.totalGuests();
        if (totalGuest > totalCapacity) {
            throw new BusinessException("Guest exceeds room capacity");
        }
    }
}

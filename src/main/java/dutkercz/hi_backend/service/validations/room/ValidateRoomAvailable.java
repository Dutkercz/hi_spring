package dutkercz.hi_backend.service.validations.room;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.exceptions.BusinessException;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.RoomStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class ValidateRoomAvailable implements RoomValidations {
    @Override
    public void validateRoom(StayRequestDto requestDto, Room room) {
        if (room.getStatus().equals(RoomStatusEnum.OCCUPIED)) {
            throw new BusinessException("Room are occupied");
        }
    }
}

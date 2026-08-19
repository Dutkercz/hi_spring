package dutkercz.hi_backend.service.validations.room;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.model.Room;

public interface RoomValidations {

    void validateRoom(StayRequestDto requestDto, Room room);
}

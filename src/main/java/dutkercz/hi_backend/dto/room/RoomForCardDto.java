package dutkercz.hi_backend.dto.room;

import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.model.enums.RoomStatusEnum;

public record RoomForCardDto(
        Long id,
        String roomNumber,
        Integer singleBeds,
        Integer doubleBeds,
        RoomStatusEnum status,
        StayResponseDto stay
) {
}

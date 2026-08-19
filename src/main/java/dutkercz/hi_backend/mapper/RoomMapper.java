package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.room.RoomForCardDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponseDto toResponseRoomDto(Room room);

    @Mapping(target = "id", source = "room.id")
    @Mapping(target = "stay", source = "lastStay", qualifiedByName = "s" )
    @Mapping(target = "status", source = "room.status")
    RoomForCardDto toRoomForCardDto(Room room, Stay lastStay);

    @Named("s")
    @Mapping(target = "paidPrice", source = "stay.paidPrice" )
    @Mapping(target = "remainingPrice", source = "stay", qualifiedByName = "calcRp")
    StayResponseDto toStayActiveResponseDto(Stay stay);

    @Named("calcRp")
    default BigDecimal calcRemainingPrice(Stay stay) {
        if (stay != null) {
            return stay.getTotalPrice().subtract(stay.getPaidPrice());
        }
        return null;
    }


}

package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface StayMapper {

    @Mapping(target = "client", source = "client")
    @Mapping(target = "room", source = "room")
    StayResponseDto toResponse(Stay stay);

    StayResponseDto.ClientSummaryResponse toClientSummary(Client client);
    StayResponseDto.RoomSummaryResponse toRoomSummary(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", source = "client")
    @Mapping(target = "room", source = "room")
    @Mapping(target = "stayStatus", source = "status")
    @Mapping(target = "checkIn", source = "checkin")
    @Mapping(target = "checkOut", source = "checkout")
    @Mapping(target = "partialPrice", source = "valorTotal")
    @Mapping(target = "stayGuests", ignore = true)
    Stay toEntity(StayRequestDto request, Client client, Room room, LocalDateTime checkin,
                  LocalDateTime checkout, BigDecimal valorTotal, StayStatus status);
}


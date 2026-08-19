package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
    @Mapping(target = "dailyRates", source = "dailyRates")
    @Mapping(target = "checkOut", source = "checkout")
    @Mapping(target = "paidPrice", ignore = true)
    @Mapping(target = "stayGuests", ignore = true)
    @Mapping(target = "dailyPrice", source = "dailyPrice")
    Stay toEntity(StayRequestDto request, Client client, Room room, LocalDateTime checkin,
                  LocalDateTime checkout, BigDecimal dailyPrice, BigDecimal totalPrice,
                  Long dailyRates, StayStatus status);

    @AfterMapping
    default void afterMapping(StayRequestDto request, @MappingTarget Stay stay){
       if (request.isPaid()){
            stay.setPaidPrice(stay.getTotalPrice());
            stay.setIsPaid(true);
       }else {

           stay.setIsPaid(false);
           stay.setPaidPrice(BigDecimal.ZERO);

       }
    }
}


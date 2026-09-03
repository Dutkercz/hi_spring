package dutkercz.hi_backend.dto.room;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record MonthlyOccupationDto(
   String roomNumber,
   LocalDateTime checkIn,
   LocalDateTime checkOut,
   String clientName
) {
}

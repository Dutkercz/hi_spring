package dutkercz.hi_backend.dto.room;

import java.time.LocalDateTime;

public record MonthlyOccupationDto(
   String roomNumber,
   LocalDateTime checkIn,
   LocalDateTime checkOut,
   String clientName
) {
}

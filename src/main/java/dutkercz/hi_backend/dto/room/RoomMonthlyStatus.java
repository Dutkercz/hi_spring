package dutkercz.hi_backend.dto.room;

import java.time.LocalDateTime;

public record RoomMonthlyStatus(
   String roomNumber,
   LocalDateTime checkIn,
   LocalDateTime checkOut
) {
}

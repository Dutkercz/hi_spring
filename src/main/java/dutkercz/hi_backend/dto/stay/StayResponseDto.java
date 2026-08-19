package dutkercz.hi_backend.dto.stay;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StayResponseDto(
        Long id,
        ClientSummaryResponse client,
        RoomSummaryResponse room,
        LocalDateTime checkIn,
        LocalDateTime checkOut,
        Long dailyRates,
        BigDecimal dailyPrice,
        BigDecimal paidPrice,
        BigDecimal remainingPrice,
        BigDecimal totalPrice,
        String stayStatus
) {
    public record ClientSummaryResponse(Long id, String firstName, String lastName) {}
    public record RoomSummaryResponse(Long id, String roomNumber) {}
}

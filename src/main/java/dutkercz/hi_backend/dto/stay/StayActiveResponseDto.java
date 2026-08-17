package dutkercz.hi_backend.dto.stay;


import dutkercz.hi_backend.dto.client.ClientResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StayActiveResponseDto(
        Long id,
        ClientResponseDto client,
        Integer totalGuests,
        BigDecimal dailyPrice,
        LocalDateTime checkIn,
        List<StayGuestDto> stayGuests
) {
}

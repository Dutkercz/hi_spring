package dutkercz.hi_backend.dto.stay;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.hi_backend.model.StayGuest;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record StayRequestDto(
        @NotNull
        Long clientId,

        @NotNull
        Long roomId,

        @FutureOrPresent(message = "A data de check-in deve ser hoje ou no futuro")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime checkIn,

        @FutureOrPresent(message = "A data de check-in deve ser hoje ou no futuro")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime checkOut,

        @NotNull
        Integer totalGuests,

        List<StayGuest> stayGuests
) {}

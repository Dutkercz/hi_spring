package dutkercz.hi_backend.dto.stay;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.hi_backend.model.StayGuest;
import dutkercz.hi_backend.model.enums.PaymentMethod;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record StayRequestDto(
        @NotNull(message = "O identificador do cliente é obrigatório")
        Long clientId,

        @NotNull(message = "O identificador do apartamento é obrigatório")
        Long roomId,

        @NotNull(message = "A data de check-in é obrigatória")
        @FutureOrPresent(message = "A data de check-in deve ser hoje ou uma data futura" )
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime checkIn,

        @NotNull(message = "A data de check-out é obrigatória")
        @FutureOrPresent(message = "A data de check-out deve ser hoje ou uma data futura")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime checkOut,

        @NotNull(message = "A quantidade de hóspedes é obrigatória")
        Integer totalGuests,

        PaymentMethod method,

        @NotNull(message = "É obrigatório informar se a hospedagem está paga")
        Boolean isPaid,

        List<StayGuest> stayGuests
) {}

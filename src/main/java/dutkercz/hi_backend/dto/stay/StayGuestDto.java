package dutkercz.hi_backend.dto.stay;

import jakarta.validation.constraints.NotBlank;

public record StayGuestDto(@NotBlank String name) {
}

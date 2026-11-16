package dutkercz.hi_backend.dto.stay;

import jakarta.validation.constraints.NotBlank;

public record StayGuestDto(@NotBlank(message = "O nome do hóspede não pode estar em branco") String name) {
}

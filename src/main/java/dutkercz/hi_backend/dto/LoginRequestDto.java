package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "Informe um endereço de e-mail válido")
        String username,
        @NotBlank(message = "A senha não pode estar em branco")
        String password
) {
}

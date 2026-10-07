package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "O campo username não pode deve estar em branco")
        @Email(message = "Username inválido, use seu email de cadastro.")
        String username,
        @NotBlank
        String password
) {
}

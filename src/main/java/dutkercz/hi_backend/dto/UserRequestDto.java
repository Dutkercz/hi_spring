package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @NotBlank(message = "O campo email não pode estar em branco")
        @Email(message = "Informe um endereço de e-mail válido")
        String email,
        @NotBlank(message = "A senha não pode estar em branco")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,100}$",
                message = "A senha deve ter entre 6 e 100 caracteres, incluindo letras maiúsculas, minúsculas e números"
        )
        String password,
        @NotBlank(message = "O nome não pode estar em branco")
        String fullName) {
}

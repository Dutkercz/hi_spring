package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @NotBlank(message = "O campo email não pode estar em branco")
        @Email(message = "O campo email, aparenta não ser um email")
        String email,
        @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z]).{6,100}$")
        String password
) {
}

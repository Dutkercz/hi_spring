package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RecoveryPwDto(
        @NotBlank(message = "Informe o código de recuperação")
        String otpCode,
        @NotBlank(message = "Informe a nova senha")
        String password
) {
}

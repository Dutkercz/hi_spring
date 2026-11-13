package dutkercz.hi_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record RecoveryPwDto(
        @NotBlank(message = "Por favor, informe o código OTP")
        String otpCode,
        @NotBlank(message = "Informe a sua nova senha")
        String password
) {
}

package dutkercz.hi_backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.UserRole;

import java.time.Instant;
import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String email,
        @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss")
        LocalDateTime createdAt,
        UserRole role
) {
    public UserResponseDto(User user) {
        this(user.getId(), user.getEmail(), user.getCreatedAt(), user.getRole());
    }
}

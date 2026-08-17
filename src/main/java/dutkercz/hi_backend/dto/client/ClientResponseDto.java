package dutkercz.hi_backend.dto.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.hi_backend.dto.address.AddressResponseDto;
import dutkercz.hi_backend.model.enums.ClientStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public record ClientResponseDto(
        Long id,
        String idClient,
        String firstName,
        String lastName,
        String cnpj,
        String phoneNumber,
        String status,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,
        List<AddressResponseDto> addresses
) {
}

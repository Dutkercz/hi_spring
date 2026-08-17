package dutkercz.hi_backend.dto.client;

import dutkercz.hi_backend.dto.address.AddressRequestDto;

import java.util.List;

public record ClientRequestDto(
        String firstName,
        String lastName,
        String cpf,
        String cnpj,
        String phoneNumber,
        List<AddressRequestDto> addresses

) {
}

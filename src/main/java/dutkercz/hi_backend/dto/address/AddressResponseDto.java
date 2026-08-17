package dutkercz.hi_backend.dto.address;

public record AddressResponseDto(
        Long id,
        String zipCode,
        String street,
        String number,
        String city,
        String state
) {
}

package dutkercz.hi_backend.dto.address;

public record AddressRequestDto(
        String zipCode,
        String street,
        String number,
        String city,
        String state) {
}

package dutkercz.hi_backend.dto.room;

public record RoomResponseDto(
        Long id,
        String roomNumber,
        Integer singleBeds,
        Integer doubleBeds) {
}

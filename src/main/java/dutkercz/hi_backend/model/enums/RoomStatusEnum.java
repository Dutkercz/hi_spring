package dutkercz.hi_backend.model.enums;

public enum RoomStatusEnum {
    OCCUPIED,
    AVAILABLE,
    MAINTENANCE,
    RESERVED,
    ;

    public boolean cantDeleteRoom() {
        return this == RoomStatusEnum.OCCUPIED;
    }
}

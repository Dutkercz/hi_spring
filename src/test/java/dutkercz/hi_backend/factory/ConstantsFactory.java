package dutkercz.hi_backend.factory;

import java.time.LocalDateTime;

public class ConstantsFactory {

    public static final LocalDateTime CHECKIN = LocalDateTime
            .of(2026, 10, 15, 13, 30, 0 );
    public static final LocalDateTime CHECKOUT = CHECKIN.plusDays(1).withHour(11)
                                                        .withMinute(0).withSecond(0);
}

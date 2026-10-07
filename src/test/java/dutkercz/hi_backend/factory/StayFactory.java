package dutkercz.hi_backend.factory;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Room;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;


public class StayFactory {

    public static Stay createStay(Room room, BigDecimal dailyPrice, Long dailyRates, Client client,
                                  BigDecimal paidPrice, BigDecimal totalPrice, boolean isPaid) {
        var checkIn = ConstantsFactory.CHECKIN;
        var checkOut = ConstantsFactory.CHECKOUT;
       return new Stay(null, client, room , dailyPrice ,paidPrice, totalPrice,
                       isPaid, 1, dailyRates, StayStatus.CURRENT,
                       checkIn, checkOut, new ArrayList<>(), new ArrayList<>());
    }

    public static StayRequestDto createStayRequest(Long clientId){
        return new StayRequestDto(clientId, 1L, ConstantsFactory.CHECKIN, ConstantsFactory.CHECKOUT,
                                  1, false, new ArrayList<>());

    }
}

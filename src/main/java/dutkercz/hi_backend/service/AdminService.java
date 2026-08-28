package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.repository.DailyPriceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DailyPriceRepository dailyPriceRepository;

    @Transactional
    public void adjustDailyPrice(@Valid DailyPricesDto request) {
        var dailyPrices = dailyPriceRepository.findAll().getLast();

        dailyPrices.setOneGuestPrice(request.oneGuestPrice());
        dailyPrices.setTwoGuestPrice(request.twoGuestPrice());
        dailyPrices.setThreeGuestPrice(request.threeGuestPrice());
        dailyPrices.setFourGuestPrice(request.fourGuestPrice());
    }

}

package dutkercz.hi_backend.mapper;

import dutkercz.hi_backend.dto.DailyPricesResponse;
import dutkercz.hi_backend.model.DailyPrices;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface DailyPriceMapper {

    DailyPricesResponse toResponse(DailyPrices dailyPrices);
}

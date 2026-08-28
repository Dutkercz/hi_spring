package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.dto.DailyPricesResponse;
import dutkercz.hi_backend.dto.stay.StayPayment;
import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.service.StayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class StayController {

    private final StayService stayService;

    @PostMapping
    public ResponseEntity<StayResponseDto> newStay(@RequestBody StayRequestDto requestDto,
                                                   UriComponentsBuilder builder) {
        StayResponseDto responseDto = stayService.newStay(requestDto);
        URI uri = builder.path("/api/stays/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PatchMapping("/{id}/add-payment-amount")
    public ResponseEntity<StayResponseDto> addPaymentAmount(@PathVariable Long id, @RequestBody StayPayment amount) {
        return ResponseEntity.ok(stayService.addPayment(id, amount));
    }

    @GetMapping("/daily-prices")
    public ResponseEntity<DailyPricesResponse> dailyPrice(){
        return ResponseEntity.ok(stayService.dailyPrices());
    }

    @PatchMapping("/checkout/{id}")
    public ResponseEntity<Void> stayCheckout(@PathVariable Long id){
        stayService.checkout(id);
        return  ResponseEntity.ok().build();
    }

    @PatchMapping("/update-daily-rates/{id}")
    public ResponseEntity<StayResponseDto> updateStay(@PathVariable Long id){
        return ResponseEntity.ok(stayService.updateDailyRates(id));
    }

}


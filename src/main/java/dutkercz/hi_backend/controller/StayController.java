package dutkercz.hi_backend.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import dutkercz.hi_backend.dto.DailyPricesResponse;
import dutkercz.hi_backend.dto.room.MonthlyOccupationDto;
import dutkercz.hi_backend.dto.room.RoomResponseDto;
import dutkercz.hi_backend.dto.stay.RefundDto;
import dutkercz.hi_backend.dto.stay.StayPayment;
import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.service.StayService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Slf4j
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

    @PutMapping("/add-daily/{id}")
    public ResponseEntity<StayResponseDto> addDaily(@PathVariable Long id){
        return ResponseEntity.ok(stayService.addStay(id));
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

    @GetMapping("/monthly-occupation")
    public ResponseEntity<List<MonthlyOccupationDto>> monthlyOccupationBoard(@PathParam(value = "year") Integer year,
                                                                             @PathParam(value = "month") Integer month){
        log.info("data {} / {}", year, month);
        return ResponseEntity.ok(stayService.roomMonthlyStatus(year, month));
    }

    @PatchMapping("/refund/{id}")
    public ResponseEntity<StayResponseDto> refundStayAmount(@PathVariable Long id, @RequestBody RefundDto refundDto){
        log.info("Values {} / {}", id, refundDto);
        return ResponseEntity.ok(stayService.refundStayAmount(id, refundDto));
    }
}


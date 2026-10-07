package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.dto.DailyPricesUpdateDto;
import dutkercz.hi_backend.dto.admin.MonthlyResume;
import dutkercz.hi_backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/daily-prices")
    public ResponseEntity<DailyPricesDto> getDailyPrices() {
        return ResponseEntity.ok(adminService.getDailyPrices());
    }

    @PatchMapping
    public ResponseEntity<DailyPricesDto> adjustDailyPrice(@RequestBody @Valid DailyPricesUpdateDto request)
            throws BadRequestException {
        return ResponseEntity.ok(adminService.adjustDailyPrice(request));
    }

    @GetMapping("/month-resume")
    public ResponseEntity<MonthlyResume> monthResume(@RequestParam Integer year,
            @RequestParam Integer month) {
        return ResponseEntity.ok(adminService.totalStaysAmountPerMonth(year, month));
    }
}

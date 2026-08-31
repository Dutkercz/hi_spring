package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.dto.admin.MonthlyResume;
import dutkercz.hi_backend.service.AdminService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping
    public ResponseEntity<Void> adjustDailyPrice(@RequestBody @Valid DailyPricesDto request){
        adminService.adjustDailyPrice(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/month-resume")
    public ResponseEntity<MonthlyResume> monthResume(@PathParam(value = "year") Integer year,
                                                     @PathParam(value = "month") Integer month){
        return ResponseEntity.ok(adminService.totalStaysAmountPerMonth(year, month));
    }
}

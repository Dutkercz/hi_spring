package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PatchMapping
    public ResponseEntity<Void> adjustDailyPrice(@RequestBody @Valid DailyPricesDto request){
        adminService.adjustDailyPrice(request);
        return ResponseEntity.ok().build();
    }
}

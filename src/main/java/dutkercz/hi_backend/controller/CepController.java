package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.CepResponseData;
import dutkercz.hi_backend.service.CepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/cep")
@RequiredArgsConstructor
public class CepController {

    private final CepService cepService;

    @GetMapping("/{cep}")
    public ResponseEntity<CepResponseData> getCep(@PathVariable String cep) {
        log.info("getCep {}", cep);
        return ResponseEntity.ok(cepService.findCepData(cep));
    }

}

package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.dto.stay.StayResponseDto;
import dutkercz.hi_backend.service.StayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}


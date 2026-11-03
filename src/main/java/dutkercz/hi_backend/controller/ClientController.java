package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.client.ClientDetailDto;
import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.client.ClientResponseDto;
import dutkercz.hi_backend.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@RequestBody ClientRequestDto requestDto,
                                                          UriComponentsBuilder builder) {
        ClientResponseDto clientResponseDto = clientService.createClient(requestDto);
        URI uri =  builder.path("/api/clients/{id}").buildAndExpand(clientResponseDto.id()).toUri();
        return ResponseEntity.created(uri).body(clientResponseDto);
    }


    @GetMapping("/{cpf}")
    public ResponseEntity<ClientResponseDto> findClientByCpf(@PathVariable String cpf){
        var responseDto = clientService.findByCpf(cpf);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientResponseDto>> searchClients(@RequestParam(name = "param") String param) {
        var clients = clientService.findClients(param);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ClientDetailDto> findClientById(@PathVariable Long id){
        return ResponseEntity.ok(clientService.getClientDetails(id));
    }
}

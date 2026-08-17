package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.client.ClientResponseDto;
import dutkercz.hi_backend.mapper.ClientMapper;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public Page<ClientResponseDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toClientResponseDto);
    }

    public ClientResponseDto createClient(ClientRequestDto requestDto) {
        log.info("Creating client with name: {}", requestDto.firstName());
        Client client = clientRepository.save(clientMapper.toClient(requestDto));
        client.getAddresses().forEach(address -> address.setClient(client));
        log.info("Created client with date: {}", client.getCreatedAt());
        return clientMapper.toClientResponseDto(client);
    }

    public ClientResponseDto findByCpf(String cpf) {
        String normCPf = cpf.trim().replaceAll("[.-]", "");
        return clientMapper.toClientResponseDto(getByCpf(normCPf));
    }

    public Client getByCpf(String cpf){
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new EntityNotFoundException("Client not found with cpf " + cpf));
    }
}

package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.client.ClientDetailDto;
import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.client.ClientResponseDto;
import dutkercz.hi_backend.exceptions.ResourceNotFoundException;
import dutkercz.hi_backend.mapper.ClientMapper;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.repository.ClientRepository;
import dutkercz.hi_backend.repository.StayRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final StayRepository stayRepository;

    public Page<ClientResponseDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::toClientResponseDto);
    }

    public ClientResponseDto createClient(ClientRequestDto requestDto) {
        Client client = clientRepository.save(clientMapper.toClient(requestDto));
        client.getAddresses().forEach(address -> address.setClient(client));
        return clientMapper.toClientResponseDto(client);
    }

    public ClientResponseDto findByCpf(String cpf) {
        String normCPf = cpf.trim().replaceAll("[.-]", "");
        return clientMapper.toClientResponseDto(getByCpf(normCPf));
    }

    public Client getByCpf(String cpf){
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new ResourceNotFoundException("Client not found with cpf " + cpf));
    }

    public List<ClientResponseDto> findClients(String param) {
        System.out.println("param:" + param);
        if(param.replaceAll("[./-]", "").matches("\\d{11}")){
            return getClientByCpf(param);
        }
        else if (param.replaceAll("[./-]", "").matches("\\d{12,16}")) {
            return getClientByCnpj(param);
        }
        return clientRepository.findTop10ByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(param, param)
                               .stream().map(clientMapper::toClientResponseDto).toList();
    }

    private List<ClientResponseDto> getClientByCnpj(String param) {
        return clientRepository.findTop10ByCnpj((param))
                               .stream().map(clientMapper::toClientResponseDto).toList();
    }

    private List<ClientResponseDto> getClientByCpf(String param) {
        var clients =  List.of(clientRepository.findByCpf(param).orElseThrow(() ->
                                            new ResourceNotFoundException("Client not found")));
        return clients.stream().map(clientMapper::toClientResponseDto).toList();
    }

    public ClientDetailDto getClientDetails(Long id){
        var client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found "));
        var stays = stayRepository.findAllByClientId(client.getId());
        return clientMapper.toClientDetail(client, stays);
    }
}

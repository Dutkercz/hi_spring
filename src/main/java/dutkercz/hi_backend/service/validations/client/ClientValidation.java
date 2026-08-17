package dutkercz.hi_backend.service.validations.client;

import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientValidation {

    private final ClientRepository clientRepository;

    public Client validateWithId(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id " + id));
    }

    public Client validateWithCpf(String cpf){
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with cpf " + cpf));
    }
}

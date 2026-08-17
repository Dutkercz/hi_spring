package dutkercz.hi_backend.service;

import dutkercz.hi_backend.dto.CepResponseData;
import dutkercz.hi_backend.exceptions.CepNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CepService {

    private final RestClient restClient = RestClient.create("https://viacep.com.br/ws/");

    public CepResponseData findCepData(String cep) {
        try {
            return restClient.get()
                             .uri("{cep}/json", cep)
                             .retrieve()
                             .body(CepResponseData.class);
        } catch (HttpClientErrorException e) {
            throw new CepNotExistException("Cep not exist or is incorrect " + e.getMessage());
        }
    }
}

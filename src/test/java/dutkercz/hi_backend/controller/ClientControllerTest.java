package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.factory.ClientFactory;
import dutkercz.hi_backend.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser
@SpringBootTest
class ClientControllerTest extends DefaultAbstractContainerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ClientRequestDto> requestJacksonTester;
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Transactional
    void createClientReturnsCreatedClient() throws Exception {
        var request = ClientFactory.createClientRequest();
        mockMvc.perform(post("/api/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJacksonTester.write(request).getJson()))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.firstName").value("Cristian"))
               .andExpect(jsonPath("$.lastName").value("Rosa"))
               .andExpect(jsonPath("$.addresses").isEmpty());
    }

    @Test
    @Transactional
    void findClientByCpfReturnsExistingClient() throws Exception {
        clientRepository.save(ClientFactory.createClientWithoutId());

        mockMvc.perform(get("/api/v1/clients/{cpf}", "12345678900"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("Cristian"))
               .andExpect(jsonPath("$.lastName").value("Rosa"));
    }

    @Test
    @Transactional
    void findClientByUnknownCpfReturnsNotFound() throws Exception {
        String result = mockMvc.perform(get("/api/v1/clients/{cpf}", "99999999999"))
                       .andExpect(status().isNotFound())
                       .andReturn()
                       .getResponse().getContentAsString();
        assertTrue(result.contains("Client not found with cpf "));

    }
}

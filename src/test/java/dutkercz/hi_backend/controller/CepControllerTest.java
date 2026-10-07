package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.CepResponseData;
import dutkercz.hi_backend.exceptions.CepNotExistException;
import dutkercz.hi_backend.service.CepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest
class CepControllerTest extends DefaultAbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CepService cepService;

    @Test
    void getCepReturnsServiceData() throws Exception {
        var cepResponse = new CepResponseData("96450000", "", "Dom Pedrito", "RS");
        when(cepService.findCepData("96450000"))
                .thenReturn(cepResponse);

        mockMvc.perform(get("/api/v1/cep/96450000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zipCode").value("96450000"))
                .andExpect(jsonPath("$.city").value("Dom Pedrito"))
                .andExpect(jsonPath("$.state").value("RS"));
    }

    @Test
    void getCepReturnsExceptWhenIsIncorrect() throws Exception {
        when(cepService.findCepData("964500"))
                .thenThrow(new CepNotExistException("Cep not exist or is incorrect"));

        mockMvc.perform(get("/api/v1/cep/964500")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

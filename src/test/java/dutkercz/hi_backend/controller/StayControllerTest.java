package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.client.ClientRequestDto;
import dutkercz.hi_backend.dto.stay.RefundDto;
import dutkercz.hi_backend.dto.stay.StayPayment;
import dutkercz.hi_backend.dto.stay.StayRequestDto;
import dutkercz.hi_backend.factory.ClientFactory;
import dutkercz.hi_backend.factory.ConstantsFactory;
import dutkercz.hi_backend.factory.StayFactory;
import dutkercz.hi_backend.model.enums.PaymentMethod;
import dutkercz.hi_backend.repository.ClientRepository;
import dutkercz.hi_backend.repository.RoomRepository;
import dutkercz.hi_backend.repository.StayRepository;
import dutkercz.hi_backend.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static dutkercz.hi_backend.factory.StayFactory.createStay;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@WithMockUser
class StayControllerTest extends DefaultAbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JacksonTester<StayRequestDto> stayJacksonTester;
    @Autowired
    private JacksonTester<StayPayment> paymentJacksonTester;
    @Autowired
    private JacksonTester<RefundDto> refundJacksonTester;

    @Test
    @Transactional
    void newStayReturnsCreatedStayAndOccupiesRoom() throws Exception {
        var client = clientRepository.save(ClientFactory.createClientWithoutId());
        var stayRequest = StayFactory.createStayRequest(client.getId());

        mockMvc.perform(post("/api/v1/stays")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stayJacksonTester.write(stayRequest).getJson()))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.client.firstName").value("Cristian"))
               .andExpect(jsonPath("$.room.roomNumber").value("1"))
               .andExpect(jsonPath("$.dailyRates").value(1))
               .andExpect(jsonPath("$.totalPrice").isNumber())
               .andExpect(jsonPath("$.isPaid").value(false))
               .andExpect(jsonPath("$.stayStatus").value("CURRENT"));
    }

    @Test
    @Transactional
    void monthlyOccupationReturnsEmptyArrayWhenThereAreNoStays() throws Exception {
        mockMvc.perform(get("/api/v1/stays/monthly-occupation")
                .param("year", "2025")
                .param("month", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @Transactional
    @Description("should return Not Found for unknow client")
    void newStayReturnsNotFoundForUnknownClient() throws Exception {
        var stayRequest = StayFactory.createStayRequest(9999L);

        String result = mockMvc.perform(
                post("/api/v1/stays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stayJacksonTester.write(stayRequest).getJson()))
                .andExpect(status().isNotFound())
                .andReturn().getResponse()
                               .getContentAsString();
        assertTrue(result.contains("Client not found with id"));
    }

    @Test
    @Transactional
    @Description("should return Not Found for unknow stay when adding a daily rate")
    void shouldReturnNotFoundWhenAddingDaily() throws Exception {
        String result = mockMvc.perform(put("/api/v1/stays/add-daily/999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getContentAsString();
        assertTrue(result.contains("Stay with id 999999 not found"));
    }

    @Test
    @Transactional
    @Description("should return NOT FOND for unknown Stay when adding payment amount")
    void shouldReturnNotFoundWhenAddingPaymentAmount() throws Exception {
        var paymentRequest = new StayPayment(PaymentMethod.CASH, new BigDecimal("50.00"));

        mockMvc.perform(patch("/api/v1/stays/999999/add-payment-amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJacksonTester.write(paymentRequest).getJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Description("Should return not found for a invalid stay at checkout")
    void shouldReturnNotFoundWhenCheckout() throws Exception {
        String result = mockMvc.perform(patch("/api/v1/stays/checkout/999999"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse().getContentAsString();
        assertTrue(result.contains("Stay with id 999999 not found"));

    }

    @Test
    @Transactional
    @Description("Should return not found for a invalid stay when try to update")
    void shouldReturnNotFoundWhenUpdatingDailyRates() throws Exception {
        String result =
                mockMvc.perform(patch("/api/v1/stays/update-daily-rates/999999"))
                       .andExpect(status().isNotFound())
                       .andReturn()
                       .getResponse().getContentAsString();
        assertTrue(result.contains("Stay with id 999999 not found"));
    }

    @Test
    @Transactional
    @Description("Should return not found for unknow stay when try to refunding")
    void shouldReturnNotFoundWhenRefunding() throws Exception {
        var refundDto = new RefundDto(new BigDecimal("50.0"));

        var result = mockMvc.perform(patch("/api/v1/stays/refund/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refundJacksonTester.write(refundDto).getJson()))
                .andExpect(status().isNotFound())
                .andReturn()
               .getResponse().getContentAsString();
        assertTrue(result.contains("Stay with id 999999 not found"));
    }

    @Test
    @Transactional
    @Description("Should mark stay with PAID at add a full payment for")
    void addFullPaymentMarksStayAsPaid() throws Exception {
        var room = roomRepository.findById(1L).orElseThrow();
        var client = clientRepository.save(ClientFactory.createClientWithoutId());
        var stay = stayRepository.save( createStay(room, new BigDecimal(160), 1L,
                    client, new BigDecimal(0), new BigDecimal(160), false));

        var paymentRequest = new StayPayment(PaymentMethod.CASH,
                                             new BigDecimal("160.00"));

        mockMvc.perform(patch("/api/v1/stays/{id}/add-payment-amount", stay.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentJacksonTester.write(paymentRequest).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paidPrice").value(160.0))
                .andExpect(jsonPath("$.remainingPrice").value(0.0))
                .andExpect(jsonPath("$.isPaid").value(true))
                .andDo(print());
    }

    @Test
    @Transactional
    void refundReducesPaidAmountAndClearsPaidFlag() throws Exception {
        var room = roomRepository.findById(1L).orElseThrow();
        var client = clientRepository.save(ClientFactory.createClientWithoutId());
        var stay = stayRepository.save(StayFactory.createStay(room,new BigDecimal(160),
              1L, client, new BigDecimal(210),new BigDecimal(160),true));

        var refundRequest = new RefundDto(new BigDecimal("50.00"));

        mockMvc.perform(patch("/api/v1/stays/refund/{id}", stay.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(refundJacksonTester.write(refundRequest).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paidPrice").value(160.0))
                .andExpect(jsonPath("$.remainingPrice").value(0.0))
                .andExpect(jsonPath("$.isPaid").value(true  ));
    }


}

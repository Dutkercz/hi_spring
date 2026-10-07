package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.DailyPricesDto;
import dutkercz.hi_backend.factory.ClientFactory;
import dutkercz.hi_backend.factory.ConstantsFactory;
import dutkercz.hi_backend.factory.StayFactory;
import dutkercz.hi_backend.model.DailyPrices;
import dutkercz.hi_backend.model.Client;
import dutkercz.hi_backend.model.Stay;
import dutkercz.hi_backend.model.enums.StayStatus;
import dutkercz.hi_backend.repository.ClientRepository;
import dutkercz.hi_backend.repository.DailyPriceRepository;
import dutkercz.hi_backend.repository.RoomRepository;
import dutkercz.hi_backend.repository.StayRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser(roles = "ADMIN", username = "test@testemail.com", password = "testpassword123ABC")
class AdminControllerTest extends DefaultAbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DailyPriceRepository dailyPriceRepository;
    @Autowired
    private StayRepository stayRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private JacksonTester<DailyPricesDto> requestTest;

    @Test
    @Description("Should return status OK with all most recently daily prices")
    @Transactional
    void getAllDailyPricesWithSuccess() throws Exception {
        var dailyPrices = new DailyPrices(1L,
                new BigDecimal("150.0"),
                new BigDecimal("250.0"),
                new BigDecimal("350.0"),
                new BigDecimal("450.0"));
        dailyPriceRepository.save(dailyPrices);
        mockMvc.perform(get("/api/v1/admin/daily-prices")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.oneGuestPrice").value("150.0"))
                .andExpect(jsonPath("$.twoGuestPrice").value("250.0"))
                .andExpect(jsonPath("$.threeGuestPrice").value("350.0"))
                .andExpect(jsonPath("$.fourGuestPrice").value("450.0"))
                .andDo(print());
    }

    @Test
    @Transactional
    void updateDailyPrices() throws Exception {
        var dailyPrices = new DailyPrices(1L,
                new BigDecimal("150.0"),
                new BigDecimal("250.0"),
                new BigDecimal("350.0"),
                new BigDecimal("450.0"));
        dailyPriceRepository.save(dailyPrices);

        var updateDailyPrices = new DailyPricesDto(
                1L,
                new BigDecimal("260.0"),
                new BigDecimal("360.0"),
                new BigDecimal("460.0"),
                new BigDecimal("560.0"));

        mockMvc.perform(patch("/api/v1/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestTest.write(updateDailyPrices).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oneGuestPrice").value("260.0"))
                .andExpect(jsonPath("$.twoGuestPrice").value("360.0"))
                .andExpect(jsonPath("$.threeGuestPrice").value("460.0"))
                .andExpect(jsonPath("$.fourGuestPrice").value("560.0"))
                .andDo(print());
    }

    @Test
    @Transactional
    @Description("Should throws a exception when ID is invalid")
    void notUpdateDailyPrices() throws Exception {
        var dailyPrices = new DailyPrices(1L,
                new BigDecimal("150.0"),
                new BigDecimal("250.0"),
                new BigDecimal("350.0"),
                new BigDecimal("450.0"));
        dailyPriceRepository.save(dailyPrices);

        var updateDailyPrices = new DailyPricesDto(
                99L, // id inexistentes
                new BigDecimal("260.0"),
                new BigDecimal("360.0"),
                new BigDecimal("460.0"),
                new BigDecimal("560.0"));

        mockMvc.perform(patch("/api/v1/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestTest.write(updateDailyPrices).getJson()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Transactional
    @Description("Should reject a non-positive price")
    void notUpdateDailyPricesWithNonPositivePrice() throws Exception {
        var updateDailyPrices = new DailyPricesDto(
                1L,
                BigDecimal.ZERO,
                new BigDecimal("360.0"),
                new BigDecimal("460.0"),
                new BigDecimal("560.0"));

        mockMvc.perform(patch("/api/v1/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestTest.write(updateDailyPrices).getJson()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void monthResumeIncludesPreviousDecemberAndFractionalOccupancy() throws Exception {
        var room = roomRepository.findById(1L).orElseThrow();
        var defaultPrice = new BigDecimal("160.0");
        var client = clientRepository.save(ClientFactory.createClientWithoutId());

        //hospedagem de comparação no mes anterior
        Stay lastMonthStay = StayFactory.createStay(room, defaultPrice, 1L, client,
                                                    defaultPrice, defaultPrice, true);
        lastMonthStay.setCheckIn(lastMonthStay.getCheckIn().minusMonths(1));
        lastMonthStay.setCheckOut(lastMonthStay.getCheckOut().minusMonths(1));
        System.out.println(lastMonthStay.getCheckIn());
        stayRepository.save(lastMonthStay);

        //hopedagem 1
        stayRepository.save(StayFactory.createStay(room, defaultPrice, 1L,  client,
                                   defaultPrice, defaultPrice,true  ));

        mockMvc.perform(get("/api/v1/admin/month-resume")
                .param("year", "2026")
                .param("month", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMonthProfit").value(defaultPrice))
                .andExpect(jsonPath("$.percentageChange").value(0))
                .andExpect(jsonPath("$.lastMonthProfit").value(defaultPrice))
                .andExpect(jsonPath("$.actualMonthDailyRates").value(1))
                .andExpect(jsonPath("$.lastMonthDailyRates").value(1))
                .andExpect(jsonPath("$.percentageActualMonthlyOccupation").value(0.25))
                .andExpect(jsonPath("$.percentageLastMonthlyOccupation").value(0.26))
                .andDo(print());
    }
}

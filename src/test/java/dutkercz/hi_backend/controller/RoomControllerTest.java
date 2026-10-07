package dutkercz.hi_backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WithMockUser
@SpringBootTest
class RoomControllerTest extends DefaultAbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void getAllRoomsReturnsSeededRooms() throws Exception {
        mockMvc.perform(get("/api/v1/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(13))
                .andExpect(jsonPath("$[0].roomNumber").value("1"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));
    }

    @Test
    @Transactional
    void updateRoomConfigReturnsUpdatedRoom() throws Exception {
        mockMvc.perform(patch("/api/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"singleBeds": 3, "doubleBeds": 2}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.singleBeds").value(3))
                .andExpect(jsonPath("$.doubleBeds").value(2));
    }
}

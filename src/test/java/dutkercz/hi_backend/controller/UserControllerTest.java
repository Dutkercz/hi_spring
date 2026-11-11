package dutkercz.hi_backend.controller;

import dutkercz.hi_backend.dto.UserRequestDto;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.UserRole;
import dutkercz.hi_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser(username = "emailteste@email.com", roles = "ADMIN")
class UserControllerTest extends DefaultAbstractContainerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JacksonTester<UserRequestDto> userJacksonTester;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void shouldRegisterUserWithSuccess() throws Exception {
        var userRequest = new UserRequestDto("emailteste@email.com", "1234Abcd", "cris");

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                                             .content(userJacksonTester.write(userRequest).getJson()))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.email").value("emailteste@email.com"))
               .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @Transactional
    @Description("Should fail when try register a user with EMAIL already registered")
    void shouldNotRegisterUserWithSuccess() throws Exception {
        var initialUserCount = userRepository.count();
        userRepository.save(
                new User(null, "Cris", "file", "emailteste@email.com", "1234Abcd", true, LocalDateTime.now(), null,
                         UserRole.USER,
                         null));

        var userRequest = new UserRequestDto("emailteste@email.com", "1234Abcd", "cris");
        mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userJacksonTester.write(userRequest).getJson()))
               .andExpect(status().isConflict());
        assertEquals(initialUserCount + 1, userRepository.count());
    }

    @Test
    @Transactional
    @Description("Should fail when try register a user with PW out of pattern")
    void shouldNotRegisterUserPwNotInPatterns() throws Exception {
        var initialUserCount = userRepository.count();
        var userRequest = new UserRequestDto("emailteste@email.com", "12d", "cris");
        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                                             .content(userJacksonTester.write(userRequest).getJson()))
               .andExpect(status().isBadRequest());
        assertEquals(initialUserCount, userRepository.count());
    }

    @Test
    @Transactional
    @Description("Should fail when try register a user with EMAIL out of pattern")
    void shouldNotRegisterUserEmailNotInPatterns() throws Exception {
        var initialUserCount = userRepository.count();
        var userRequest = new UserRequestDto("emailemail.com", "1234Abcd",  "cris");
        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                                             .content(userJacksonTester.write(userRequest).getJson()))
               .andExpect(status().isBadRequest());
        assertEquals(initialUserCount, userRepository.count());
    }

    @Test
    @Transactional
    @Description("Should return the authenticated USER successfully")
    void shouldReturnAuthenticatedUser() throws Exception {
        User user = userRepository.save(new User(null,"cris", null, "emailteste@email.com",
             "1234Abcd", true, LocalDateTime.now(), null, UserRole.USER,
                         null));
        var userId = user.getId();

        var testAuthentication = new UsernamePasswordAuthenticationToken("emailteste@email.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        mockMvc.perform(get("/api/v1/users")
                                .with(authentication(testAuthentication))
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(userId))
               .andExpect(jsonPath("$.email").value("emailteste@email.com"))
               .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @Transactional
    @Description("Should NOT return a USER when id not exist")
    void shouldReturnNotFoundForUnsupportedUserPath() throws Exception {
        var userId = 1L;
        mockMvc.perform(get("/api/v1/users/{id}", userId)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }
}

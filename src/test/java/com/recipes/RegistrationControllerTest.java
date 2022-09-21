package com.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.model.User;
import com.recipes.repository.UserRepository;
import com.recipes.service.RecipeService;
import com.recipes.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegistrationControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private RecipeService recipeCreateServiceMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Value("${sql.scripts.create.user}")
    String sqlAddUser;

    @Value("${sql.script.delete.user}")
    String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddUser);
    }

    @Test
    public void registerNewUserHttpRequest() throws Exception {
        User user = new User("CamelCaseRecipe@somewhere.com","C00k1234es","USER");

        this.mockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        User verifyUser = userRepository.findUserByEmail("CamelCaseRecipe@somewhere.com");
        assertEquals("CamelCaseRecipe@somewhere.com", verifyUser.getEmail());
    }

    @Test
    public void registerNewUserHttpRequestUsernameExistBadRequestResponse() throws Exception {
        User user = new User("Cook_Programmer@somewhere.com","C00k1234es","USER");

        this.mockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerNewUserHttpRequestInvalidPasswordBadRequestResponse() throws Exception {
        User user = new User("CamelCaseRecipe@somewhere.com", "C00k", "USER");

        this.mockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }
}

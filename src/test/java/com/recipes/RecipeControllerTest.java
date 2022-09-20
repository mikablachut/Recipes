package com.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.model.Recipe;
import com.recipes.repository.RecipeRepository;
import com.recipes.service.RecipeService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecipeControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    private RecipeService recipeCreateServiceMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Value("${sql.script.create.recipes}")
    private String sqlAddRecipe;

    @Value("${sql.script.create.recipe_ingredients_zero}")
    private String sqlAddRecipeIngredientsZero;

    @Value("${sql.script.create.recipe_ingredients_one}")
    private String sqlAddRecipeIngredientsOne;

    @Value("${sql.script.create.recipe_ingredients_two}")
    private String sqlAddRecipeIngredientsTwo;

    @Value("${sql.script.create.recipe_directions_zero}")
    private String sqlAddRecipeDirectionsZero;

    @Value("${sql.script.create.recipe_directions_one}")
    private String sqlAddRecipeDirectionsOne;

    @Value("${sql.script.create.recipe_directions_two}")
    private String sqlAddRecipeDirectionsTwo;

    @Value("${sql.script.create.recipe_directions_three}")
    private String sqlAddRecipeDirectionsThree;

    @Value("${sql.script.create.recipe_directions_four}")
    private String sqlAddRecipeDirectionsFour;

    @Value("${sql.script.delete.recipes}")
    private String sqlDeleteRecipes;

    @Value("${sql.script.delete.recipe_directions}")
    private String sqlDeleteRecipeDirections;

    @Value("${sql.script.delete.recipe_ingredients}")
    private String sqlDeleteRecipeIngredients;

    @Value("${sql.scripts.create.user}")
    String sqlAddUser;

    @Value("${sql.script.delete.user}")
    String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddRecipe);
        jdbc.execute(sqlAddRecipeIngredientsZero);
        jdbc.execute(sqlAddRecipeIngredientsOne);
        jdbc.execute(sqlAddRecipeIngredientsTwo);
        jdbc.execute(sqlAddRecipeDirectionsZero);
        jdbc.execute(sqlAddRecipeDirectionsOne);
        jdbc.execute(sqlAddRecipeDirectionsTwo);
        jdbc.execute(sqlAddRecipeDirectionsThree);
        jdbc.execute(sqlAddRecipeDirectionsFour);
        jdbc.execute(sqlAddUser);
    }

    @Test
    @WithMockUser(username = "Cook_Programmer@somewhere.com", password = "password1234")
    public void getRecipeHttpRequest() throws Exception {
        Recipe recipe = recipeRepository.findRecipeById(2L);

        assertNotNull(recipe);

        this.mockMvc.perform(get("/api/recipe/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Fresh Mint Tea")))
                .andExpect(jsonPath("$.category", is("beverage")))
                .andExpect(jsonPath("$.date", is("2022-09-15T12:48:33.101901")))
                .andExpect(jsonPath("$.description", is("Light, aromatic and refreshing beverage, ...")))
                .andExpect(jsonPath("$.ingredients", hasSize(3)))
                .andExpect(jsonPath("$.directions", hasSize(5)));
    }

    @Test
    @WithMockUser(username = "Cook_Programmer@somewhere.com", password = "password1234")
    public void getRecipeHttpRequestRecipeDoesNotExistEmptyResponse() throws Exception {
        Recipe recipe = recipeRepository.findRecipeById(1L);

        assertNull(recipe);

        this.mockMvc.perform(get("/api/recipe/{id}",1))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getRecipeHttpRequestWithoutAuthentication() throws Exception {
        this.mockMvc.perform(get("/api/recipe/{id}",2))
                .andExpect(status().is(401));

    }

    @Test
    @WithMockUser(username = "Cook_Programmer@somewhere.com", password = "password1234")
    public void searchRecipeByCategoryOrNameHttpRequest() throws Exception {
        List<Recipe> listOfRecipe = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc("beverage");
        List<Recipe> listOfRecipeByName = recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc("tea");

        assertEquals(1, listOfRecipe.size());
        assertEquals(1, listOfRecipeByName.size());

        this.mockMvc.perform(get("/api/recipe/search/").contentType(MediaType.APPLICATION_JSON)
                .param("category", "beverage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        this.mockMvc.perform(get("/api/recipe/search/").contentType(MediaType.APPLICATION_JSON)
                        .param("name", "tea"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "Cook_Programmer@somewhere.com", password = "password1234")
    public void searchRecipeByNameOrCategoryHttpRequestEmptyCategoryAndNameResponse() throws Exception {
        List<Recipe> listOfRecipe = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(null);
        List<Recipe> listOfRecipeByName = recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(null);

        assertEquals(0, listOfRecipe.size());
        assertEquals(0, listOfRecipeByName.size());

        this.mockMvc.perform(get("/api/recipe/search/"))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(get("/api/recipe/search/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void searchRecipeHttpRequestWithoutAuthentication() throws Exception {
        this.mockMvc.perform(get("/api/recipe/search/").contentType(MediaType.APPLICATION_JSON)
                        .param("category", "beverage"))
                        .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "Cook_Programmer@somewhere.com", password = "password1234")
    public void addRecipeHttpRequest() throws Exception {
        String[] ingredients = new String[]{"1 inch ginger root, minced", "1/2 lemon, juiced",
                "1/2 teaspoon manuka honey"};
        String[] directions = new String[]{"Place all ingredients in a mug and fill with" +
                " warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes",
                "Drink and enjoy"};
        Recipe recipe = new Recipe("Warming Ginger Tea", "beverage", LocalDateTime.now(),
                "Ginger tea is a warming drink for cool weather, ...", ingredients, directions,
                "CamelCaseRecipe@somewhere.com");

        this.mockMvc.perform(post("/api/recipe/new").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        Recipe verifyRecipe = recipeRepository.findRecipeById(1L);

        assertNotNull(verifyRecipe);
    }

    @Test
    public void addRecipeHttpRequestWithoutAuthentication() throws Exception {
        String[] ingredients = new String[]{"1 inch ginger root, minced", "1/2 lemon, juiced",
                "1/2 teaspoon manuka honey"};
        String[] directions = new String[]{"Place all ingredients in a mug and fill with" +
                " warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes",
                "Drink and enjoy"};
        Recipe recipe = new Recipe("Warming Ginger Tea", "beverage", LocalDateTime.now(),
                "Ginger tea is a warming drink for cool weather, ...", ingredients, directions,
                "CamelCaseRecipe@somewhere.com");

        this.mockMvc.perform(post("/api/recipe/new").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().is(401));
    }





    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteRecipeDirections);
        jdbc.execute(sqlDeleteRecipeIngredients);
        jdbc.execute(sqlDeleteRecipes);
        jdbc.execute(sqlDeleteUser);
    }












}

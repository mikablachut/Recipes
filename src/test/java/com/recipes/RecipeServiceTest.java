package com.recipes;

import com.recipes.model.Recipe;
import com.recipes.repository.RecipeRepository;
import com.recipes.service.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class RecipeServiceTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeService recipeService;

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
    }

    @Test
    public void getRecipeByIdService() {
        LocalDateTime local = LocalDateTime.of(2022,9,15,12,48,33,
                101901000);

        Recipe recipe = recipeService.findRecipeById(2L);
        Recipe recipeNull = recipeService.findRecipeById(0L);

        assertNotNull(recipe, "Recipe exist, result should not be null");
        assertNull(recipeNull, "Recipe does not exist, result should be null");

        assertEquals(2, recipe.getId());
        assertEquals("Fresh Mint Tea", recipe.getName());
        assertEquals("beverage", recipe.getCategory());
        assertEquals(local, recipe.getDate());
        assertEquals("Light, aromatic and refreshing beverage, ...", recipe.getDescription());
        assertArrayEquals(new String[]{"boiled water", "honey", "fresh mint leaves"}, recipe.getIngredients());
        assertArrayEquals(new String[]{"Boil water", "Pour boiling hot water into a mug",
                "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"},
                recipe.getDirections());
        assertEquals("Cook_Programmer@somewhere.com", recipe.getAuthor());
    }

    @Test
    public void saveRecipeService() {
        String[] ingredients = new String[]{"1 inch ginger root, minced", "1/2 lemon, juiced",
                "1/2 teaspoon manuka honey"};
        String[] directions = new String[]{"Place all ingredients in a mug and fill with" +
                " warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes",
                "Drink and enjoy"};
        Recipe recipe = new Recipe("Warming Ginger Tea", "beverage", LocalDateTime.now(),
                "Ginger tea is a warming drink for cool weather, ...", ingredients, directions,
                "CamelCaseRecipe@somewhere.com");

        recipeService.save(recipe);

        Recipe savedRecipe = recipeRepository.findRecipeById(1L);

        assertEquals(1, savedRecipe.getId());

    }

    @Test
    public void deleteRecipeService() {
        Recipe deletedRecipe = recipeRepository.findRecipeById(2L);

        assertNotNull(deletedRecipe);

        recipeService.deleteRecipeById(2L);

        deletedRecipe = recipeRepository.findRecipeById(2L);

        assertNull(deletedRecipe);

    }

    @Test
    public void isRecipeExistService() {
        assertTrue(recipeService.existsById(2L), "@BeforeTransaction creates recipe : return true");
        assertFalse(recipeService.existsById(0L), "No recipe should have 0 id : return false");
    }

    @Sql("/insertData.sql")
    @Test
    public void findRecipeByCategoryService() {
        List<Recipe> recipeByExistingCategory = recipeService.findByCategoryIgnoreCaseOrderByDateDesc("beverage");
        List<Recipe> recipeByNotExistingCategory = recipeService.findByCategoryIgnoreCaseOrderByDateDesc("dinner");

        assertEquals(2, recipeByExistingCategory.size());
        assertEquals(0, recipeByNotExistingCategory.size());

    }

    @Sql("/insertData.sql")
    @Test
    public void findRecipeByNameService() {
        List<Recipe> recipeByExistFullName = recipeService.findByNameIgnoreCaseContainsOrderByDateDesc("Fresh Mint Tea");
        List<Recipe> recipeByExistNotFullNameInLowerCase = recipeService.findByNameIgnoreCaseContainsOrderByDateDesc
                ("fresh mint");
        List<Recipe> recipeByExistOneWord = recipeService.findByNameIgnoreCaseContainsOrderByDateDesc("tea");
        List<Recipe> recipeByNoExistName = recipeService.findByNameIgnoreCaseContainsOrderByDateDesc("Fresh Orange Tea");

        assertEquals(1, recipeByExistFullName.size());
        assertEquals(1, recipeByExistNotFullNameInLowerCase.size());
        assertEquals(2, recipeByExistOneWord.size());
        assertEquals(0, recipeByNoExistName.size());

    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteRecipeDirections);
        jdbc.execute(sqlDeleteRecipeIngredients);
        jdbc.execute(sqlDeleteRecipes);
    }
}

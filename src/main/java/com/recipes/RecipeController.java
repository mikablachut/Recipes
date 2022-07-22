package com.recipes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ConcurrentHashMap;

@RestController
public class RecipeController {
    private ConcurrentHashMap<RecipeID, Recipe> listOfRecipes = new ConcurrentHashMap<>();

    @GetMapping("/api/recipe/{id}")
    public Recipe returnRecipe(@PathVariable int id) {
        RecipeID recipeID = new RecipeID(id);
        if (!listOfRecipes.containsKey(recipeID)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return listOfRecipes.get(recipeID);
        }
    }

    @PostMapping("/api/recipe/new")
    public RecipeID addRecipe(@RequestBody Recipe recipe) {
        Recipe userRecipe = new Recipe(recipe.getName(), recipe.getDescription(), recipe.getIngredients(),
                recipe.getDirections());
        RecipeID recipeID = new RecipeID(listOfRecipes.size() + 1);
        listOfRecipes.put(recipeID, userRecipe);
        return recipeID;
    }
}

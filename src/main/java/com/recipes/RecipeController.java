package com.recipes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {
    Recipe firstRecipe = new Recipe();

    @GetMapping("/api/recipe")
    public Recipe returnRecipe() {
        return firstRecipe;
    }

    @PostMapping("/api/recipe")
    public void addRecipe(@RequestBody Recipe recipe) {
        firstRecipe.setName(recipe.getName());
        firstRecipe.setDescription(recipe.getDescription());
        firstRecipe.setIngredients(recipe.getIngredients());
        firstRecipe.setDirections(recipe.getDirections());
    }
}

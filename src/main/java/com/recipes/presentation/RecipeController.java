package com.recipes.presentation;

import com.recipes.businesslayer.Recipe;
import com.recipes.businesslayer.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;

@RestController
public class RecipeController {
    @Autowired
    RecipeService recipeService;

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        try {
            if (recipeService.existsById(id)) {
                return recipeService.findRecipeById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<String> addRecipe(@Valid @RequestBody Recipe recipe) {
        try {
            Recipe createdRecipe = recipeService.save(new Recipe(recipe.getId(), recipe.getName(), recipe.getDescription(),
                    recipe.getIngredients(), recipe.getDirections()));
            return new ResponseEntity<>("{\"id\": " + createdRecipe.getId() + "}", HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable long id) {
        try {
            recipeService.deleteRecipeById(id);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
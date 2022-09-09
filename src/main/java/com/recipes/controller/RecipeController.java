package com.recipes.controller;

import com.recipes.model.Recipe;
import com.recipes.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/api/recipe/search/")
    public List<Recipe> searchRecipe(@RequestParam(required = false) String category,
                                     @RequestParam(required = false) String name) {

        Optional<String> nameCheck = Optional.ofNullable(name);
        Optional<String> categoryCheck = Optional.ofNullable(category);

        if ((nameCheck.isEmpty() && categoryCheck.isEmpty())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (nameCheck.isPresent()) {
            return recipeService.findByNameIgnoreCaseContainsOrderByDateDesc(name);
        } else {
            return recipeService.findByCategoryIgnoreCaseOrderByDateDesc(category);
        }
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<String> addRecipe(@AuthenticationPrincipal UserDetails userDetails,
                                            @Valid @RequestBody Recipe recipe) {
        String name = userDetails.getUsername();

        try {
            Recipe createdRecipe = recipeService.save(new Recipe(recipe.getId(), recipe.getName(),recipe.getCategory(),
                                                      LocalDateTime.now(), recipe.getDescription(),
                                                      recipe.getIngredients(), recipe.getDirections(), name));
            return new ResponseEntity<>("{\"id\": " + createdRecipe.getId() + "}", HttpStatus.OK);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        if (recipeService.existsById(id)) {
            if (recipeService.findRecipeById(id).getAuthor().equals(userDetails.getUsername())) {
                try {
                    recipeService.deleteRecipeById(id);
                } catch (RuntimeException e) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/recipe/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id,
                             @Valid @RequestBody Recipe recipe) {
        if (recipeService.existsById(id)) {
            if (recipeService.findRecipeById(id).getAuthor().equals((userDetails.getUsername()))) {
                try {
                    Recipe recipeToUpdate = recipeService.findRecipeById(id);
                    recipeToUpdate.setName(recipe.getName());
                    recipeToUpdate.setCategory(recipe.getCategory());
                    recipeToUpdate.setDate(LocalDateTime.now());
                    recipeToUpdate.setDescription(recipe.getDescription());
                    recipeToUpdate.setIngredients(recipe.getIngredients());
                    recipeToUpdate.setDirections(recipe.getDirections());
                    recipeService.save(recipeToUpdate);
                } catch (RuntimeException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}

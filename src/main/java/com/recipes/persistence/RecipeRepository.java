package com.recipes.persistence;

import com.recipes.businesslayer.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    Recipe findRecipeById(Long id);
    Recipe save(Recipe toSave);
    void deleteById(Long id);
    boolean existsById(Long id);
}

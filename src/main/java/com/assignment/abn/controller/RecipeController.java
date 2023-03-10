package com.assignment.abn.controller;

import com.assignment.abn.domain.Recipe;
import com.assignment.abn.dto.RecipeDTO;
import com.assignment.abn.dto.SearchRequest;
import com.assignment.abn.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController {
    
    private final RecipeService recipeService;
    
    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable int id) {
        return recipeService.getRecipe(id);
    }
    
    @PutMapping("/{id}")
    public Recipe updateRecipe(@RequestBody RecipeDTO recipe, @PathVariable Long id) {
        return recipeService.saveOrUpdateRecipe(recipe, id);
    }
    
    @PostMapping("/")
    public Recipe saveRecipe(@RequestBody RecipeDTO recipe) {
        return recipeService.saveOrUpdateRecipe(recipe, -1L);
    }
    
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
    
    @PostMapping("/search")
    public List<Recipe> searchRecipe(@RequestBody SearchRequest searchRequest) {
        return recipeService.searchRecipe(searchRequest);
    }
}

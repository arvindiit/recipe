package com.assignment.abn.controller;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.dto.IngredientDTO;
import com.assignment.abn.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ingredient")
public class IngredientController {
    
    private final IngredientService ingredientService;
    
    @GetMapping("/{id}")
    public Ingredient getIngredient(@PathVariable int id) {
        return ingredientService.getIngredient(id);
    }
    
    @PutMapping("/{id}")
    public Ingredient updateIngredient(@RequestBody IngredientDTO ingredient, @PathVariable Long id) {
        return ingredientService.updateIngredient(ingredient.getIngredient(), id);
    }
    
    @PostMapping("/")
    public Ingredient saveIngredient(@RequestBody IngredientDTO ingredient) {
        return ingredientService.createIngredient(ingredient.getIngredient());
    }
    
    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
    }
}

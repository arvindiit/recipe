package com.assignment.abn.service;


import com.assignment.abn.exception.NotFoundException;
import com.assignment.abn.repository.IngredientRepository;
import com.assignment.abn.domain.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    
    public Ingredient getIngredient(long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> new NotFoundException("No ingredient found",id));
    }
    
    public Ingredient updateIngredient(Ingredient ingredient, Long id) {
        return ingredientRepository.findById(id)
                .map(saved -> {
                    saved.setName(ingredient.getName());
                    
                    return ingredientRepository.save(ingredient);
                })
                .orElseGet(() -> {
                    ingredient.setId(id);
                    return ingredientRepository.save(ingredient);
                });
    }
    
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
    
   public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
   }
}

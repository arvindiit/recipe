package com.assignment.recipe.service;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.repository.IngredientRepository;
import com.assignment.abn.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {
    @Mock
    IngredientRepository ingredientRepository;
    
    
    @InjectMocks
    IngredientService ingredientService;
    
    @Test
    void test_createIngredient() {
        Ingredient response = new Ingredient();
        response.setId(5L);
        
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(response);
        Ingredient saved = ingredientService.createIngredient(new Ingredient());
        assertThat(saved.getId()).isSameAs(response.getId());
    }
}

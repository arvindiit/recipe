package com.assignment.recipe.service;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.domain.Recipe;
import com.assignment.abn.dto.IngredientFilter;
import com.assignment.abn.dto.RecipeDTO;
import com.assignment.abn.dto.SearchRequest;
import com.assignment.abn.exception.NotFoundException;
import com.assignment.abn.repository.IngredientRepository;
import com.assignment.abn.repository.RecipeRepository;
import com.assignment.abn.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    
    @Mock
    RecipeRepository recipeRepository;
    
    @Mock
    IngredientRepository ingredientRepository;
    
    
    @InjectMocks
    RecipeService recipeService;
    
    @Test
    void test_createRecipe_successfully() {
        RecipeDTO recipeDTO = createRecipeDTO();
        Recipe response = recipeDTO.getRecipe();
        response.setId(4L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Tomato");
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
        when(ingredientRepository.findAllByNameIn(anyList())).thenReturn(List.of(ingredient));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(response);
        
        Recipe saved = recipeService.saveOrUpdateRecipe(recipeDTO, 1L);
        
        assertThat(saved.getId()).isSameAs(response.getId());
    }
    
    @Test
    void test_updateRecipe_successfully() {
        RecipeDTO recipeDTO = createRecipeDTO();
        Recipe response = recipeDTO.getRecipe();
        response.setId(4L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Tomato");
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(response));
        when(ingredientRepository.findAllByNameIn(anyList())).thenReturn(List.of(ingredient));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(response);
    
        Recipe saved = recipeService.saveOrUpdateRecipe(recipeDTO, 1L);
        assertThat(saved.getName()).isSameAs(response.getName());
    
    }
    
    @Test
    void test_getRecipe_notFound() {
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            recipeService.getRecipe(1L);
        });
    
        String expectedMessage = "Recipe not found";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    void test_searchCriteria_veg() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(Boolean.TRUE);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setIsVeg(Boolean.TRUE);
        when(recipeRepository.isVeg(anyBoolean())).thenReturn(mock(Specification.class));
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(recipe));
    
        List<Recipe> recipes = recipeService.searchRecipe(searchRequest);
        assertTrue(recipes.get(0).getIsVeg());
    }
    
    @Test
    void test_searchCriteria_serving() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setServing(2);
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setServing(2);
        when(recipeRepository.noOfServing(anyInt())).thenReturn(mock(Specification.class));
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(recipe));
        
        List<Recipe> recipes = recipeService.searchRecipe(searchRequest);
        assertThat(recipes.get(0).getServing()).isEqualTo(2);
    }
    
    @Test
    void test_searchCriteria_instruction() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setInstruction("Oven");
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setInstruction("Oven");
        when(recipeRepository.instructionLike(anyString())).thenReturn(mock(Specification.class));
        when(recipeRepository.findAll(any(Specification.class))).thenReturn(List.of(recipe));
        
        List<Recipe> recipes = recipeService.searchRecipe(searchRequest);
        assertThat(recipes.get(0).getInstruction()).isEqualTo("Oven");
    }
    
    @Test
    void test_searchCriteria_ingredients() {
        SearchRequest searchRequest = new SearchRequest();
        IngredientFilter ingredientFilter = new IngredientFilter();
        ingredientFilter.setFilter(IngredientFilter.Filter.INCLUDE);
        ingredientFilter.setIngredients(List.of("Tomato", "Potato"));
        searchRequest.setIngredientFilter(ingredientFilter);
        
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient tomato = new Ingredient();
        tomato.setName("Tomato");
        Ingredient potato = new Ingredient();
        potato.setName("Potato");
        recipe.setRecipeIngredients(Set.of(potato, tomato));
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        
        List<Recipe> recipes = recipeService.searchRecipe(searchRequest);
        assertThat(recipes.get(0).getRecipeIngredients()).hasSize(2);
    
        ingredientFilter.setFilter(IngredientFilter.Filter.EXCLUDE);
        recipes = recipeService.searchRecipe(searchRequest);
        assertThat(recipes).hasSize(0);
    }
    
    @Test
    void test_findBySearchCriteria_notFound() {
        SearchRequest searchRequest = new SearchRequest();
        Exception exception = assertThrows(NotFoundException.class, () -> {
            recipeService.searchRecipe(searchRequest);
        });
    
        String expectedMessage = "No criteria found to search";
        String actualMessage = exception.getMessage();
    
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    private RecipeDTO createRecipeDTO() {
        RecipeDTO recipeDTO = new RecipeDTO();
        recipeDTO.setName("Name");
        recipeDTO.setInstruction("instructions");
        recipeDTO.setServing(3);
        recipeDTO.setIsVeg(Boolean.TRUE);
        recipeDTO.setIngredients(List.of("Potato", "Tomato"));
        return recipeDTO;
    }
}

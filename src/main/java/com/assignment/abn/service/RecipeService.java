package com.assignment.abn.service;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.domain.Recipe;
import com.assignment.abn.dto.IngredientFilter;
import com.assignment.abn.dto.RecipeDTO;
import com.assignment.abn.dto.SearchRequest;
import com.assignment.abn.exception.NotFoundException;
import com.assignment.abn.repository.IngredientRepository;
import com.assignment.abn.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {
    
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    
    public Recipe getRecipe(long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new NotFoundException("Recipe not found",id));
    }
    
    public Recipe saveOrUpdateRecipe(RecipeDTO recipeDTO, Long id) {
        return recipeRepository.findById(id)
                .map(saved -> {
                    Recipe recipe = recipeDTO.getRecipe();
                    saved.setName(recipe.getName());
                    saved.setIsVeg(recipe.getIsVeg());
                    saved.setServing(recipe.getServing());
                    saved.setRecipeIngredients(getIngredient(recipeDTO.getIngredients()));
                    return recipeRepository.save(recipe);
                })
                .orElseGet(() -> {
                    Recipe recipe = recipeDTO.getRecipe();
                    recipe.setRecipeIngredients(getIngredient(recipeDTO.getIngredients()));
                    return recipeRepository.save(recipe);
                });
    }
    
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
    
   
   private Set<Ingredient> getIngredient(List<String> names) {
        List<Ingredient> ingredients =  ingredientRepository.findAllByNameIn(names);
        return new HashSet<>(ingredients);
   }
   
   public List<Recipe> searchRecipe(SearchRequest searchRequest) {
        List<Specification<Recipe>> specifications = new ArrayList<>();
        if( searchRequest.getVeg() != null ) {
            specifications.add(recipeRepository.isVeg(searchRequest.getVeg()));
        }
        if(searchRequest.getServing() != null) {
            specifications.add(recipeRepository.noOfServing(searchRequest.getServing()));
        }
        if(searchRequest.getInstruction() != null && !searchRequest.getInstruction().isEmpty()){
            specifications.add(recipeRepository.instructionLike(searchRequest.getInstruction()));
        }
       /**
        * Left out for reference. Filtering on children does not work
        * if(searchRequest.getIngredientFilter() != null){
        *            if(searchRequest.getIngredientFilter().getFilter().equals(IngredientFilter.Filter.INCLUDE)) {
        *                specifications.add(recipeRepository.ingredientIn(searchRequest.getIngredientFilter().getIngredients()));
        *            } else {
        *                specifications.add(recipeRepository.ingredientNotIn(searchRequest.getIngredientFilter().getIngredients()));
        *            }
        *        }
        **/
       
       if(specifications.isEmpty() && searchRequest.getIngredientFilter() == null) {
           throw new NotFoundException("No criteria found to search");
       }
       List<Recipe> recipes;
       if(specifications.isEmpty()) {
           recipes = recipeRepository.findAll();
       } else {
           Specification<Recipe> specification = specifications.get(0);
           for(int i = 1; i < specifications.size(); i++) {
               specification = specification.and(specifications.get(i));
           }
           recipes = recipeRepository.findAll(specification);
       }
       List<Recipe> result = new ArrayList<>(recipes);
        if(searchRequest.getIngredientFilter() != null){
            for(Recipe recipe : recipes) {
                List<String> ingredients = recipe.getRecipeIngredients().stream().map(Ingredient::getName).collect(Collectors.toList());
                if ( searchRequest.getIngredientFilter().getFilter().equals(IngredientFilter.Filter.EXCLUDE) && ingredients.containsAll(searchRequest.getIngredientFilter().getIngredients())) {
                    result.remove(recipe);
                } else if(searchRequest.getIngredientFilter().getFilter().equals(IngredientFilter.Filter.INCLUDE) && !ingredients.containsAll(searchRequest.getIngredientFilter().getIngredients())) {
                    result.remove(recipe);
        
                }
            }
        }
        return result;
   }
}

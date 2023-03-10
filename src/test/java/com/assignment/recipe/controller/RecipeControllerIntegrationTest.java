package com.assignment.recipe.controller;

import com.assignment.abn.domain.Recipe;
import com.assignment.abn.dto.IngredientFilter;
import com.assignment.abn.dto.RecipeDTO;
import com.assignment.abn.dto.SearchRequest;
import com.assignment.abn.repository.IngredientRepository;
import com.assignment.abn.repository.RecipeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RecipeControllerIntegrationTest extends AbstractIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    IngredientRepository ingredientRepository;
    
    @Autowired
    RecipeRepository recipeRepository;
    
    @AfterEach
    public void tearDown(){
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }
    
    @Test
    void testSave() throws Exception {
        RecipeDTO recipe = createRecipeDTO();
        mockMvc.perform(post("/api/recipe/")
                .content(asJsonString(recipe))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test-recipe")))
                .andExpect(jsonPath("isVeg", is(true)));
    }
    
    @Test
    void testReadDelete() throws Exception {
        testSave();
        Long id = recipeRepository.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/recipe/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Test-recipe")))
                .andExpect(jsonPath("isVeg", is(true)))
                .andExpect(jsonPath("instruction", is("Oven based recipe")));
        
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/recipe/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    
        List<Recipe> results = recipeRepository.findAll();
        assertThat(results.size()).isEqualTo(0);
    }
    
    @Test
    void testSearchIsVeg() throws Exception {
        testSave();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(true);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Test-recipe")))
                .andExpect(jsonPath("$.[0].isVeg", is(true)));
    
        searchRequest.setVeg(false);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        
    }
    @Test
    void testSearchIsVegAndNoOfServing() throws Exception {
        testSave();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(true);
        searchRequest.setServing(2);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Test-recipe")))
                .andExpect(jsonPath("$.[0].serving", is(2)));
        
        searchRequest.setServing(3);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    void testSearchIsVegAndInstruction() throws Exception {
        testSave();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(true);
        searchRequest.setInstruction("Oven");
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Test-recipe")))
                .andExpect(jsonPath("$.[0].instruction",  containsString("Oven")));
    
        searchRequest.setInstruction("Electric");
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    @Sql(scripts = "classpath:db/populateDB.sql",
            config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
    void testSearchIngredientsExcludeInclude() throws Exception {
        testSave();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(true);
        IngredientFilter ingredientFilter = new IngredientFilter();
        ingredientFilter.setIngredients(List.of("Tomato"));
        ingredientFilter.setFilter(IngredientFilter.Filter.INCLUDE);
        searchRequest.setIngredientFilter(ingredientFilter);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Test-recipe")))
                .andExpect(jsonPath("$.[0].recipeIngredients",  Matchers.hasSize(2)));
    
        ingredientFilter = new IngredientFilter();
        ingredientFilter.setIngredients(List.of("Onion"));
        ingredientFilter.setFilter(IngredientFilter.Filter.INCLUDE);
        searchRequest.setIngredientFilter(ingredientFilter);        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    @Sql(scripts = "classpath:db/populateDB.sql",
            config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
    void testSearchIngredientsExclude() throws Exception {
        testSave();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setVeg(true);
        IngredientFilter ingredientFilter = new IngredientFilter();
        ingredientFilter.setIngredients(List.of("Onion"));
        ingredientFilter.setFilter(IngredientFilter.Filter.EXCLUDE);
        searchRequest.setIngredientFilter(ingredientFilter);
        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Test-recipe")))
                .andExpect(jsonPath("$.[0].recipeIngredients",  Matchers.hasSize(2)));
        
        ingredientFilter = new IngredientFilter();
        ingredientFilter.setIngredients(List.of("Tomato"));
        ingredientFilter.setFilter(IngredientFilter.Filter.EXCLUDE);
        searchRequest.setIngredientFilter(ingredientFilter);        mockMvc.perform(post("/api/recipe/search")
                .content(asJsonString(searchRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    private RecipeDTO createRecipeDTO() {
        RecipeDTO recipe = new RecipeDTO();
        recipe.setInstruction("Oven based recipe");
        recipe.setServing(2);
        recipe.setName("Test-recipe");
        recipe.setIsVeg(true);
        recipe.setIngredients(List.of("Tomato", "Potato"));
        
        return recipe;
    }
    
}

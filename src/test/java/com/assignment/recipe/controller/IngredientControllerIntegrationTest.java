package com.assignment.recipe.controller;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientControllerIntegrationTest extends AbstractIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    IngredientRepository ingredientRepository;
    
    @Test
    public void testCreateReadDelete() throws Exception {
        Ingredient ingredient = createIngredient();
        
        mockMvc.perform(post("/api/ingredient/")
                .content(asJsonString(ingredient))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Tomato")));
        
        List<Ingredient> results = ingredientRepository.findAll();
        Long id = results.get(0).getId();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getName()).isEqualTo("Tomato");
    
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/ingredient/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("Tomato")));
        
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/ingredient/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    
        results = ingredientRepository.findAll();
        assertThat(results.size()).isEqualTo(0);
    }
    
    private Ingredient createIngredient() {
        Ingredient tomato = new Ingredient();
        tomato.setName("Tomato");
        return tomato;
    }
}

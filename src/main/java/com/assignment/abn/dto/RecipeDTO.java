package com.assignment.abn.dto;

import com.assignment.abn.domain.Recipe;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class RecipeDTO {
    private String name;
    private Boolean isVeg;
    private Integer serving;
    private String instruction;
    private List<String> ingredients;
    
    @JsonIgnore
    public Recipe getRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName(this.name);
        recipe.setIsVeg(this.isVeg);
        recipe.setInstruction(this.instruction);
        recipe.setServing(this.serving);
        return  recipe;
    }
}

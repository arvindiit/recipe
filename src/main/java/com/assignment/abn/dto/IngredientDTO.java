package com.assignment.abn.dto;

import com.assignment.abn.domain.Ingredient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class IngredientDTO {
    private String name;
    
    @JsonIgnore
    public Ingredient getIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(this.name);
        return  ingredient;
    }
}

package com.assignment.abn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RecipeIngredientKey implements Serializable {
    @Column(name = "recipe_id")
    Long recipeId;
    
    @Column(name = "ingredient_id")
    Long ingredientId;
}

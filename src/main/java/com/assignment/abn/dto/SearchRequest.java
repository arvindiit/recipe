package com.assignment.abn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private Boolean veg;
    private Integer serving;
    private String instruction;
    private IngredientFilter ingredientFilter;
}

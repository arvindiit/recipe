package com.assignment.abn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngredientFilter {
    
    @NotBlank
    private List<String> ingredients;
    @NotNull
    private Filter filter;
    
    public enum Filter {
        EXCLUDE("exclude"),
        INCLUDE("include");
        
        private String value;
        
        Filter(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}

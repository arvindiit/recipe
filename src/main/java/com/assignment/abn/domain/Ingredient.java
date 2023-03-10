package com.assignment.abn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Entity
@Getter
@Setter
@DynamicUpdate
@Table(name = "ingredients")
public class Ingredient {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    
    @ManyToMany(mappedBy = "recipeIngredients", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private Set<Recipe> recipeIngredients;
}

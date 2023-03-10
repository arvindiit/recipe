package com.assignment.abn.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@DynamicUpdate
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private Boolean isVeg;
    private int serving;
    private String instruction;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id"))
    
    private Set<Ingredient> recipeIngredients;
    
    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdDate;
    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
    
}

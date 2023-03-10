package com.assignment.abn.repository;

import com.assignment.abn.domain.Ingredient;
import com.assignment.abn.domain.Recipe;
import com.assignment.abn.domain.Recipe_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    default Specification<Recipe> instructionLike(String name){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(Recipe_.INSTRUCTION), "%"+name+"%");
    }
    
    default Specification<Recipe> isVeg(boolean isVeg){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(Recipe_.IS_VEG), isVeg);
    }
    
    default Specification<Recipe> noOfServing(int serving){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(Recipe_.SERVING), serving);
    }
    
    //Left out for reference
    default Specification<Recipe> ingredientIn(List<String> ingredients){
        return (root, query, criteriaBuilder)-> {
            Join<Recipe, Ingredient> recipeIngredients = root.join(Recipe_.RECIPE_INGREDIENTS);
            return criteriaBuilder.in(recipeIngredients.get("name")).value(ingredients);
        };
    }
}

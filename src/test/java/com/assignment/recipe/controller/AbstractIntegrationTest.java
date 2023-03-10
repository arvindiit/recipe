package com.assignment.recipe.controller;


import com.assignment.abn.RecipeApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = RecipeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
    protected static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.techelevator.dao;

import com.techelevator.recipes.dao.IngredientDAO;
import com.techelevator.recipes.dao.JdbcIngredientDAO;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.model.Ingredient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCIngredientDAOIntegrationTest extends DAOIntegrationTest{

    private JdbcTemplate jdbcTemplate;
    private IngredientDAO ingredientDAO;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        ingredientDAO = new JdbcIngredientDAO(jdbcTemplate);
    }

    @Test
    public void create_ingredient() {
        Ingredient newIngredient = getIngredient(-1L);

        ingredientDAO.addIngredient(newIngredient);

        Assert.assertTrue(newIngredient.getIngredientId() > 0);
        Ingredient expectedIngredient = ingredientDAO.getIngredientById(newIngredient.getIngredientId());
        Assert.assertEquals(newIngredient, expectedIngredient);


    }

    private Ingredient getIngredient(Long ingredientId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);
        ingredient.setName("testName");
        ingredient.setCategory("testCategory");
        return ingredient;

    }

}

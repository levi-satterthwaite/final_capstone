package com.techelevator.dao;

import com.techelevator.recipes.dao.IngredientDAO;
import com.techelevator.recipes.dao.JdbcIngredientDAO;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCIngredientDAOIntegrationTest extends DAOIntegrationTest{

    private JdbcTemplate jdbcTemplate;
    private IngredientDAO ingredientDAO;
    private RecipeDAO recipeDAO;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        ingredientDAO = new JdbcIngredientDAO(jdbcTemplate);
        recipeDAO = new JdbcRecipeDAO(jdbcTemplate, ingredientDAO);

    }

    @Test
    public void create_ingredient() throws IngredientException {
        Ingredient newIngredient = getIngredient(-1L);

        ingredientDAO.addIngredient(newIngredient);

        Assert.assertTrue(newIngredient.getIngredientId() > 0);
        Ingredient expectedIngredient = ingredientDAO.getIngredientById(newIngredient.getIngredientId());
        Assert.assertEquals(newIngredient, expectedIngredient);
    }

    @Test
    public void retrieve_ingredient_by_id() throws IngredientException {
    Ingredient ingredientOne = getIngredient(-1L);
    ingredientDAO.addIngredient(ingredientOne);

    Ingredient testIngredient = ingredientDAO.getIngredientById(ingredientOne.getIngredientId());
    Assert.assertEquals(ingredientOne, testIngredient);
    }

    @Test
    public void retrieve_ingredients_by_name() throws IngredientException {
        Ingredient ingredientOne = getByName("testName");
        Ingredient ingredientTwo = getByName("testName2");
        ingredientDAO.addIngredient(ingredientOne);
        ingredientDAO.addIngredient(ingredientTwo);

        List<Ingredient> testIngredientList = ingredientDAO.getIngredientsByName("testName");
        Assert.assertTrue(testIngredientList.size() > 0);

    }

    private Ingredient getIngredient(Long ingredientId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);
        ingredient.setName("testName");
        ingredient.setCategory("testCategory");
        return ingredient;

    }

    private Ingredient getByName(String name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(-1L);
        ingredient.setName(name);
        ingredient.setCategory("testCategory");
        return ingredient;
    }

}

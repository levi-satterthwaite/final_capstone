package com.techelevator.dao;

import com.techelevator.recipes.dao.IngredientDAO;
import com.techelevator.recipes.dao.JdbcIngredientDAO;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

public class JDBCRecipeDAOIntegrationTest extends DAOIntegrationTest{

    private JdbcTemplate jdbcTemplate;
    private RecipeDAO recipeDAO;
    private IngredientDAO ingredientDAO;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        ingredientDAO = new JdbcIngredientDAO(jdbcTemplate);
        recipeDAO = new JdbcRecipeDAO(jdbcTemplate, ingredientDAO);
    }

    @Test
    public void retrieve_all_recipes() {
        List<Recipe> originalList = recipeDAO.getListOfRecipes();
        Recipe recipeOne = getRecipe(4L);
        createNewTestRecipe(recipeOne);

        //Test
        List<Recipe> recipeFromDataBase = recipeDAO.getListOfRecipes();
        //Assert
        Assert.assertEquals(originalList.size() + 1, recipeFromDataBase.size());
    }

    private void createNewTestRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipe (recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING recipe_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                sql, recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(), recipe.getInstructions(),
                recipe.getDateCreated(), recipe.getImageFileName());
        rows.next();
        recipe.setRecipeId(rows.getLong("recipe_id"));
    }

    private Recipe getRecipe(Long recipeId) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setName("testName");
        recipe.setCategory("testCategory");
        recipe.setDifficultyLevel("testDifficulty");
        recipe.setPrepTimeMin(5);
        recipe.setCookTimeMin(6);
        recipe.setServingSize(4);
        recipe.setInstructions("testInstructions");
        recipe.setDateCreated(LocalDate.of(2021, 8, 2));
        recipe.setImageFileName("testImage.jpg");
        return recipe;
    }
}

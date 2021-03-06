package com.techelevator.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.model.User;
import com.techelevator.recipes.dao.IngredientDAO;
import com.techelevator.recipes.dao.JdbcIngredientDAO;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCRecipeDAOIntegrationTest extends DAOIntegrationTest{

    private JdbcTemplate jdbcTemplate;
    private RecipeDAO recipeDAO;
    private IngredientDAO ingredientDAO;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        ingredientDAO = new JdbcIngredientDAO(jdbcTemplate);
        recipeDAO = new JdbcRecipeDAO(jdbcTemplate);
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

    @Test
    public void retrieve_recipe_by_id() throws NegativeValueException, RecipeException {
        Recipe recipeOne = getRecipe(-1L);
        recipeDAO.addRecipe(recipeOne);

        //Test
        Recipe testRecipe = recipeDAO.getRecipeById(recipeOne.getRecipeId(), recipeOne.getUserId());
        //Assert
        Assert.assertEquals(recipeOne, testRecipe);
    }

    @Test
    public void retrieve_recipes_by_name() throws NegativeValueException, RecipeException {
        Recipe recipeOne = getByName("TestName1");
        Recipe recipeTwo = getByName("TestName2");
        recipeDAO.addRecipe(recipeOne);
        recipeDAO.addRecipe(recipeTwo);

        List<Recipe> testRecipeList = recipeDAO.getRecipesByName("TestName1", recipeOne.getUserId());
        Assert.assertTrue(testRecipeList.size() > 0);
    }

    @Test
    public void add_recipe() throws NegativeValueException, RecipeException {
        Recipe newRecipe = getRecipe(-1L);

        recipeDAO.addRecipe(newRecipe);

        Assert.assertTrue(newRecipe.getRecipeId() > 0);
        Recipe expectedRecipe = recipeDAO.getRecipeById(newRecipe.getRecipeId(), newRecipe.getUserId());
        Assert.assertEquals(newRecipe, expectedRecipe);
    }

    @Test
    public void add_ingredients_to_recipe() throws IngredientException, NegativeValueException, RecipeException {
        Recipe recipeOne = getRecipe(-1L);
        recipeDAO.addRecipe(recipeOne);
        Ingredient ingredientOne = getIngredient(-1L);
        ingredientOne.setUnitMeasurement("unitMeasurement");
        ingredientOne.setQuantity(1.5);
        ingredientDAO.addIngredient(ingredientOne);
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredientOne);

        Recipe testRecipe = recipeDAO.addIngredientsToRecipe(recipeOne, ingredients);
        Assert.assertTrue(testRecipe.getIngredientList().size() > 0);
        Assert.assertEquals(testRecipe.getIngredientList().get(0), ingredientOne);
    }

    @Test
    public void update_recipe() throws IngredientException, RecipeException, NegativeValueException {
        Ingredient newIngredient = getIngredient(-1L);
        ingredientDAO.addIngredient(newIngredient);

        Recipe newRecipe = getRecipe(-1L);
        recipeDAO.addRecipe(newRecipe);

        newRecipe.setName("updatedName");
        newRecipe.getIngredientList().add(newIngredient);

        Recipe expectedRecipe = recipeDAO.updateRecipe(newRecipe, newRecipe.getUserId());

        Assert.assertEquals(expectedRecipe.getName(), "updatedName");
        Assert.assertEquals(expectedRecipe.getIngredientList().size(), 1);
    }

    @Test
    public void delete_recipe() throws RecipeException, NegativeValueException {
        Recipe newRecipe = getRecipe(-1L);
        recipeDAO.addRecipe(newRecipe);

        recipeDAO.deleteRecipe(newRecipe);

        Assert.assertEquals(recipeDAO.getRecipesByName(newRecipe.getName(), newRecipe.getUserId()).size(), 0);
    }

    private void createNewTestRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipe (recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING recipe_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                sql, recipe.getUserId(), recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(), recipe.getInstructions(),
                recipe.getDateCreated(), recipe.getImageFileName());
        rows.next();
        recipe.setRecipeId(rows.getLong("recipe_id"));
    }

    private Recipe getRecipe(Long recipeId) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setUserId(1L);
        recipe.setName("testName");
        recipe.setCategory("Entree");
        recipe.setDifficultyLevel("Easy");
        recipe.setPrepTimeMin(5);
        recipe.setCookTimeMin(6);
        recipe.setServingSize(4);
        recipe.setInstructions("testInstructions");
        recipe.setDateCreated(LocalDate.of(2021, 8, 2));
        recipe.setImageFileName("testImage.jpg");
        recipe.setIngredientList(new ArrayList<>());
        return recipe;
    }

    private Ingredient getIngredient(Long ingredientId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(ingredientId);
        ingredient.setName("testName");
        ingredient.setCategory("Entree");
        ingredient.setQuantity(1);
        ingredient.setUnitMeasurement("cups");
        return ingredient;
    }

    private Recipe getByName(String name) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(-1L);
        recipe.setUserId(1L);
        recipe.setName(name);
        recipe.setCategory("Entree");
        recipe.setDifficultyLevel("Easy");
        recipe.setPrepTimeMin(5);
        recipe.setCookTimeMin(6);
        recipe.setServingSize(4);
        recipe.setInstructions("testInstructions");
        recipe.setDateCreated(LocalDate.of(2021, 8, 2));
        recipe.setImageFileName("testImage.jpg");
        recipe.setIngredientList(new ArrayList<>());
        return recipe;
    }
}

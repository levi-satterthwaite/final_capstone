package com.techelevator.dao;

import com.techelevator.mealPlanner.dao.JdbcMealDAO;
import com.techelevator.mealPlanner.dao.MealDAO;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCMealIntegrationTest extends DAOIntegrationTest {

    private JdbcTemplate jdbcTemplate;
    private MealDAO mealDAO;
    private RecipeDAO recipeDAO;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        this.recipeDAO = new JdbcRecipeDAO(jdbcTemplate);
        this.mealDAO = new JdbcMealDAO(jdbcTemplate, recipeDAO);
    }

    @Test
    public void retrieve_all_meal_plans() throws MealException, RecipeNotFoundException {
        List<Meal> originalList = mealDAO.getListOfMeal();
        Meal testMeal = getById(-1L);
        mealDAO.addMeal(testMeal);

        List<Meal> mealFromDataBase = mealDAO.getListOfMeal();

        Assert.assertEquals(originalList.size() + 1, mealFromDataBase.size());
    }

    @Test
    public void retrieve_meal_plans_by_name() throws MealException, RecipeNotFoundException {
        List<Meal> originalList = mealDAO.getListOfMeal();
        Meal mealOne = getByName("TestName1");
        Meal mealTwo = getByName("TestName2");
        mealDAO.addMeal(mealOne);
        mealDAO.addMeal(mealTwo);

        List<Meal> testMealList = mealDAO.getMealByName("TestName1");

        Assert.assertTrue(testMealList.size() > 0);
    }

    @Test
    public void retrieve_meal_plan_by_id() throws MealException, RecipeNotFoundException {
        Meal mealOne = getById(-1L);
        mealDAO.addMeal(mealOne);

        Meal testMeal = mealDAO.getMealById(mealOne.getMealId());

        Assert.assertEquals(mealOne, testMeal);
    }

    @Test
    public void add_meal_plan() throws MealException, RecipeNotFoundException {
        Meal newMeal = getById(-1L);

        mealDAO.addMeal(newMeal);

        Assert.assertTrue(newMeal.getMealId() > 0);
        Meal expectedMeal = mealDAO.getMealById(newMeal.getMealId());
        Assert.assertEquals(newMeal, expectedMeal);
    }

    @Test
    public void add_recipes_to_meal_plan() throws NegativeValueException, MealException, RecipeException {
        Recipe newRecipe = getRecipe(-1L);
        recipeDAO.addRecipe(newRecipe);

        Meal newMeal = getById(-1L);
        mealDAO.addMeal(newMeal);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(newRecipe);

        Meal testMeal = mealDAO.addRecipesToMeal(newMeal, recipes);

        Assert.assertTrue(testMeal.getRecipeList().size() > 0);
        Assert.assertEquals(testMeal.getRecipeList().get(0), newRecipe);
    }

    private Meal getById(Long mealId) {
        Meal meal = new Meal();
        meal.setMealId(mealId);
        meal.setName("testName");
        meal.setCategory("testDescription");
        meal.setImageFileName("testImage.jpg");
        meal.setRecipeList(new ArrayList<>());
        return meal;
    }

    private Meal getByName(String name) {
        Meal meal = new Meal();
        meal.setMealId(-1L);
        meal.setName(name);
        meal.setCategory("testDescription");
        meal.setImageFileName("testImage.jpg");
        meal.setRecipeList(new ArrayList<>());
        return meal;
    }

    private Recipe getRecipe(Long recipeId) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setName("testName");
        recipe.setCategory("Appetizer");
        recipe.setDifficultyLevel("Easy");
        recipe.setPrepTimeMin(5);
        recipe.setCookTimeMin(20);
        recipe.setServingSize(4);
        recipe.setInstructions("testInstructions");
        recipe.setDateCreated(LocalDate.of(2021, 8, 4));
        recipe.setImageFileName("testImage.jpg");
        recipe.setIngredientList(new ArrayList<>());
        return recipe;
    }

}

package com.techelevator.dao;

import com.techelevator.mealPlanner.dao.JdbcMealPlanDAO;
import com.techelevator.mealPlanner.dao.MealPlanDAO;
import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.model.Recipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCMealPlanIntegrationTest extends DAOIntegrationTest {

    private JdbcTemplate jdbcTemplate;
    private MealPlanDAO mealPlanDAO;
    private RecipeDAO recipeDAO;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        this.recipeDAO = new JdbcRecipeDAO(jdbcTemplate);
        this.mealPlanDAO = new JdbcMealPlanDAO(jdbcTemplate, recipeDAO);
    }

    @Test
    public void retrieve_all_meal_plans() throws MealPlanException {
        List<MealPlan> originalList = mealPlanDAO.getListOfMealPlans();
        MealPlan testMealPlan = getById(-1L);
        mealPlanDAO.addMealPlan(testMealPlan);

        List<MealPlan> mealPlanFromDataBase = mealPlanDAO.getListOfMealPlans();

        Assert.assertEquals(originalList.size() + 1, mealPlanFromDataBase.size());
    }

    @Test
    public void retrieve_meal_plans_by_name() throws MealPlanException {
        List<MealPlan> originalList = mealPlanDAO.getListOfMealPlans();
        MealPlan mealPlanOne = getByName("TestName1");
        MealPlan mealPlanTwo = getByName("TestName2");
        mealPlanDAO.addMealPlan(mealPlanOne);
        mealPlanDAO.addMealPlan(mealPlanTwo);

        List<MealPlan> testMealPlanList = mealPlanDAO.getMealPlansByName("TestName1");

        Assert.assertTrue(testMealPlanList.size() > 0);
    }

    @Test
    public void retrieve_meal_plan_by_id() throws MealPlanException {
        MealPlan mealPlanOne = getById(-1L);
        mealPlanDAO.addMealPlan(mealPlanOne);

        MealPlan testMealPlan = mealPlanDAO.getMealPlanById(mealPlanOne.getMealId());

        Assert.assertEquals(mealPlanOne, testMealPlan);
    }

    @Test
    public void add_meal_plan() throws MealPlanException {
        MealPlan newMealPlan = getById(-1L);

        mealPlanDAO.addMealPlan(newMealPlan);

        Assert.assertTrue(newMealPlan.getMealId() > 0);
        MealPlan expectedMealPlan = mealPlanDAO.getMealPlanById(newMealPlan.getMealId());
        Assert.assertEquals(newMealPlan, expectedMealPlan);
    }

    @Test
    public void add_recipes_to_meal_plan() throws NegativeValueException, MealPlanException {
        Recipe newRecipe = getRecipe(-1L);
        recipeDAO.addRecipe(newRecipe);

        MealPlan newMealPlan = getById(-1L);
        mealPlanDAO.addMealPlan(newMealPlan);

        List<Recipe> recipes = new ArrayList<>();
        recipes.add(newRecipe);

        MealPlan testMealPlan = mealPlanDAO.addRecipesToMealPlan(newMealPlan, recipes);

        Assert.assertTrue(testMealPlan.getRecipeList().size() > 0);
        Assert.assertEquals(testMealPlan.getRecipeList().get(0), newRecipe);
    }

    private MealPlan getById(Long mealId) {
        MealPlan mealPlan = new MealPlan();
        mealPlan.setMealId(mealId);
        mealPlan.setName("testName");
        mealPlan.setDescription("testDescription");
        mealPlan.setRecipeList(new ArrayList<>());
        return mealPlan;
    }

    private MealPlan getByName(String name) {
        MealPlan mealPlan = new MealPlan();
        mealPlan.setMealId(-1L);
        mealPlan.setName(name);
        mealPlan.setDescription("testDescription");
        mealPlan.setRecipeList(new ArrayList<>());
        return mealPlan;
    }

    private Recipe getRecipe(Long recipeId) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(recipeId);
        recipe.setName("testName");
        recipe.setCategory("testCategory");
        recipe.setDifficultyLevel("testDifficulty");
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

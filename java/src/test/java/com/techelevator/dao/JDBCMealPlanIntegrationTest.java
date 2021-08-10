package com.techelevator.dao;

import com.techelevator.mealPlanner.dao.JdbcMealDAO;
import com.techelevator.mealPlanner.dao.JdbcMealPlanDAO;
import com.techelevator.mealPlanner.dao.MealDAO;
import com.techelevator.mealPlanner.dao.MealPlanDAO;
import com.techelevator.mealPlanner.exceptions.InvalidMealException;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealCategory;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.dao.JdbcRecipeDAO;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JDBCMealPlanIntegrationTest extends DAOIntegrationTest {

    private JdbcTemplate jdbcTemplate;
    private MealPlanDAO mealPlanDAO;
    private MealDAO mealDAO;
    private RecipeDAO recipeDAO;

    @Before
    public void setup() {
        this.jdbcTemplate = new JdbcTemplate(getDataSource());
        this.recipeDAO = new JdbcRecipeDAO(jdbcTemplate);
        this.mealDAO = new JdbcMealDAO(jdbcTemplate, recipeDAO);
        this.mealPlanDAO = new JdbcMealPlanDAO(jdbcTemplate, mealDAO);
    }

    @Test
    public void retrieve_meal_plans_by_name() throws InvalidMealException, MealPlanException, RecipeNotFoundException, MealNotFoundException {
        MealPlan mealPlanOne = getByName("TestName1");
        MealPlan mealPlanTwo = getByName("TestName2");
        mealPlanDAO.addMealPlan(mealPlanOne);
        mealPlanDAO.addMealPlan(mealPlanTwo);

        List<MealPlan> testMealPlanList = mealPlanDAO.getMealPlanByName("TestName1", mealPlanOne.getUserId());

        Assert.assertTrue(testMealPlanList.size() > 0);
    }

    @Test
    public void retrieve_meal_plan_by_id() throws InvalidMealException, MealPlanException, MealNotFoundException, RecipeNotFoundException {
        MealPlan mealPlanOne = getById(-1L);
        mealPlanDAO.addMealPlan(mealPlanOne);

        MealPlan testMealPlan = mealPlanDAO.getMealPlanById(mealPlanOne.getMealPlanId(), mealPlanOne.getUserId());

        Assert.assertEquals(mealPlanOne, testMealPlan);
    }

    @Test
    public void add_meal_plan() throws InvalidMealException, MealPlanException, MealNotFoundException, RecipeNotFoundException {
        MealPlan newMealPlan = getById(-1L);

        mealPlanDAO.addMealPlan(newMealPlan);

        Assert.assertTrue(newMealPlan.getMealPlanId() > 0);
        MealPlan expectedMealPlan = mealPlanDAO.getMealPlanById(newMealPlan.getMealPlanId(), newMealPlan.getUserId());
        Assert.assertEquals(newMealPlan, expectedMealPlan);
    }

    @Test
    public void add_meals_to_mealPlan() throws MealException, MealPlanException, RecipeNotFoundException {
        Meal newMeal = getMealById(-1L);
        mealDAO.addMeal(newMeal);

        MealPlan newMealPlan = getById(-1L);
        mealPlanDAO.addMealPlan(newMealPlan);

        List<Meal> meals = new ArrayList<>();
        meals.add(newMeal);

        MealPlan testMealPlan = mealPlanDAO.addMealsToMealPlan(newMealPlan, meals);

        Assert.assertTrue(testMealPlan.getMealList().size() > 0);
        Assert.assertEquals(testMealPlan.getMealList().get(0), newMeal);
    }

    @Test
    public void update_meal_plan() throws MealException, MealPlanException, NegativeValueException, RecipeNotFoundException {
        Meal newMeal = getMealById(-1L);
        mealDAO.addMeal(newMeal);

        MealPlan newMealPlan = getById(-1L);
        mealPlanDAO.addMealPlan(newMealPlan);

        newMealPlan.setName("updatedName");
        newMealPlan.getMealList().add(newMeal);

        MealPlan expectedMealPlan = mealPlanDAO.updateMealPlan(newMealPlan, newMealPlan.getUserId());

        Assert.assertEquals(expectedMealPlan.getName(), "updatedName");
        Assert.assertEquals(expectedMealPlan.getMealList().size(), 1);

    }

    @Test
    public void delete_meal_plan() throws InvalidMealException, MealPlanException, MealNotFoundException, RecipeNotFoundException {
        MealPlan newMealPlan = getById(-1L);
        mealPlanDAO.addMealPlan(newMealPlan);

        mealPlanDAO.deleteMealPlan(newMealPlan);

        Assert.assertEquals(mealPlanDAO.getMealPlanByName(newMealPlan.getName(), newMealPlan.getUserId()).size(), 0);
    }

    private Meal getMealById(Long mealId) {
        Meal meal = new Meal();
        meal.setMealId(mealId);
        meal.setUserId(1L);
        meal.setName("testName");
        meal.setCategory(MealCategory.LUNCH.toString());
        meal.setImageFileName("testImage.jpg");
        meal.setRecipeList(new ArrayList<>());
        return meal;
    }

    private MealPlan getByName(String name) {
        MealPlan mealPlan = new MealPlan();
        mealPlan.setMealPlanId(-1L);
        mealPlan.setUserId(1L);
        mealPlan.setName(name);
        mealPlan.setDescription("testDescription");
        mealPlan.setImageFileName("testImage.jpg");
        mealPlan.setMealList(new ArrayList<>());
        return mealPlan;
    }
    private MealPlan getById(Long mealPlanId) {
        MealPlan mealPlan = new MealPlan();
        mealPlan.setMealPlanId(mealPlanId);
        mealPlan.setUserId(1L);
        mealPlan.setName("testName");
        mealPlan.setDescription("testDescription");
        mealPlan.setImageFileName("testImage.jpg");
        mealPlan.setMealList(new ArrayList<>());
        return mealPlan;
    }
}

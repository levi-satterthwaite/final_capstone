package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealAlreadyExistsException;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.*;
import com.techelevator.recipes.model.Recipe;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcMealDAO implements MealDAO {

    private JdbcTemplate jdbcTemplate;
    private RecipeDAO recipeDAO;

    public JdbcMealDAO(JdbcTemplate jdbcTemplate, RecipeDAO recipeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.recipeDAO = recipeDAO;
    }

    @Override
    public List<Meal> getListOfMeal() throws RecipeNotFoundException {
        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal_id, user_id, name, category, image_file_name FROM meal";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while(rows.next()) {
            Meal meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId(), meal.getUserId()));
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public List<Meal> getMealByName(String name, Long userId) throws RecipeNotFoundException {
        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal_id, user_id, name, category, image_file_name FROM meal WHERE name ILIKE ? AND user_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%", userId);
        while(rows.next()) {
            Meal meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId(), userId));
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public Meal getMealById(Long mealId, Long userId) throws MealNotFoundException, RecipeNotFoundException {
        Meal meal = null;
        String sql = "SELECT meal_id, user_id, name, category, image_file_name FROM meal WHERE meal_id = ? AND user_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId, userId);
        if(rows.next()) {
            meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId(), userId));
        }
        if(meal == null) {
            throw new MealNotFoundException();
        }
        return meal;
    }

    @Override
    public Meal addMeal(Meal meal) throws MealException {
        try {
            meal.validate();
            String sql = "INSERT INTO meal (meal_id, user_id, name, category, image_file_name) VALUES " +
                    "(DEFAULT, ?, ?, ?, ?) RETURNING meal_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, meal.getUserId(), meal.getName(), meal.getCategory(),
                    meal.getImageFileName());
            rows.next();
            meal.setMealId(rows.getLong("meal_id"));
            return meal;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new MealAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public Meal addRecipesToMeal(Meal meal, List<Recipe> recipes) throws MealNotFoundException,
            RecipeNotFoundException {
        for(Recipe recipe : recipes) {
            addRecipeToMeal(meal, recipe);
        }
        return getMealById(meal.getMealId(), meal.getUserId());
    }

    @Override
    public Meal updateMeal(Meal meal, Long userId) throws MealException, RecipeException, NegativeValueException {
        if(meal.getMealId().equals(null)) {
            throw new MealNotFoundException();
        }
        meal.validateRecipe();
        meal.validate();

        List<Recipe> existingRecipeList = getRecipesByMealId(meal.getMealId(), userId);
        Map<Long, Recipe> existingRecipesMap = new HashMap<Long, Recipe>();
        for(Recipe existingRecipe : existingRecipeList) {
            existingRecipesMap.put(existingRecipe.getRecipeId(), existingRecipe);
        }

        for(Recipe mealRecipe : meal.getRecipeList()) {
            if(!existingRecipesMap.containsKey(mealRecipe.getRecipeId())) {
                addRecipeToMeal(meal, mealRecipe);
            }
            else if(existingRecipesMap.containsKey(mealRecipe.getRecipeId())) {
                Recipe existingRecipe = existingRecipesMap.get(mealRecipe.getRecipeId());
                if(!existingRecipe.equals(mealRecipe)) {
                    updateMealRecipe(meal, mealRecipe);
                }
                existingRecipesMap.remove(mealRecipe.getRecipeId());
            }
        }
        List<Recipe> mealRecipesToRemove = new ArrayList<>(existingRecipesMap.values());
        deleteRecipesFromMeal(meal, mealRecipesToRemove);

        String sql = "UPDATE meal SET name = ?, category = ?, image_file_name = ? WHERE meal_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, meal.getName(), meal.getCategory(), meal.getImageFileName(),
            meal.getMealId(), meal.getUserId());
        return getMealById(meal.getMealId(), meal.getUserId());
    }

    @Override
    public void deleteMeal(Meal meal) throws MealException {
        makeSureMealIsNotInAMealPlan(meal);
        deleteRecipesFromMeal(meal, meal.getRecipeList());
        // delete the meal plan
        String sql = "DELETE FROM meal WHERE meal_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, meal.getMealId(), meal.getUserId());
    }

    @Override
    public void deleteRecipesFromMeal(Meal meal, List<Recipe> recipes) {
        // loop over list of ingredients
        for(Recipe recipe : recipes) {
            // check to see if the ingredient is being used in a recipe
            if(doesMealHaveRecipe(meal, recipe)) {
                // if it does delete call deleteIngredientFromRecipe
                deleteRecipeFromMeal(meal, recipe);
            }
        }
    }

    private void makeSureMealIsNotInAMealPlan(Meal meal) throws MealException {
        String sql = "SELECT meal_plan_id, meal_id FROM meal_plan_meal WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, meal.getMealId());
        if(rows.next()) {
            throw new MealInUseException();
        }
    }

    private void updateMealRecipe(Meal meal, Recipe recipe) throws NegativeValueException {
        try {
            String sql = "UPDATE recipe_meal WHERE meal_id = ? AND recipe_id = ?";
            jdbcTemplate.update(sql, meal.getMealId(), recipe.getRecipeId());
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
        }
    }

    private boolean doesMealHaveRecipe(Meal meal, Recipe recipe) {
        // check database to see if a recipe is using the ingredient
        String sql = "SELECT meal_id, recipe_id FROM recipe_meal WHERE meal_id = ? AND recipe_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, meal.getMealId(), recipe.getRecipeId());
        if(rows.next()) {
            return true;
        }
        return false;
    }

    private void deleteRecipeFromMeal(Meal meal, Recipe recipe) {
        // execute sql statement to remove the record from the join table
        String sql = "DELETE FROM recipe_meal WHERE meal_id = ? AND recipe_id = ?";
        jdbcTemplate.update(sql, meal.getMealId(), recipe.getRecipeId());
    }

    private void addRecipeToMeal(Meal meal, Recipe recipe) {
        String sql = "INSERT INTO recipe_meal (recipe_id, meal_id) VALUES " +
                "(?, ?) RETURNING meal_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId(), meal.getMealId());
        rows.next();
    }

    private List<Recipe> getRecipesByMealId(Long mealId, Long userId) throws RecipeNotFoundException {
        List<Recipe> recipes = new ArrayList<Recipe>();
        String sql = "SELECT recipe_id " +
                "FROM recipe_meal WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId);
        while(rows.next()) {
            Long recipeId = rows.getLong("recipe_id");
            Recipe recipe = recipeDAO.getRecipeById(recipeId, userId);
            recipes.add(recipe);
        }
        return recipes;
    }

    private Meal mapMealPlan(SqlRowSet row) {
        Meal meal = new Meal();

        meal.setMealId(row.getLong("meal_id"));
        meal.setUserId(row.getLong("user_id"));
        meal.setName(row.getString("name"));
        meal.setCategory(row.getString("category"));
        meal.setImageFileName(row.getString("image_file_name"));

        return meal;
    }
}

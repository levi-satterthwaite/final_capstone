package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealAlreadyExistsException;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
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
        String sql = "SELECT meal_id, name, category, image_file_name FROM meal";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while(rows.next()) {
            Meal meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId()));
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public List<Meal> getMealByName(String name) throws RecipeNotFoundException {
        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal_id, name, category, image_file_name FROM meal WHERE name ILIKE ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%");
        while(rows.next()) {
            Meal meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId()));
            meals.add(meal);
        }
        return meals;
    }

    @Override
    public Meal getMealById(Long mealId) throws MealNotFoundException, RecipeNotFoundException {
        Meal meal = null;
        String sql = "SELECT meal_id, name, category, image_file_name FROM meal WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId);
        if(rows.next()) {
            meal = mapMealPlan(rows);
            meal.setRecipeList(getRecipesByMealId(meal.getMealId()));
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
            String sql = "INSERT INTO meal (meal_id, name, category, image_file_name) VALUES " +
                    "(DEFAULT, ?, ?, ?) RETURNING meal_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, meal.getName(), meal.getCategory(),
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
        return getMealById(meal.getMealId());
    }

    @Override
    public Meal updateMeal(Meal meal) throws MealException, RecipeException, NegativeValueException {
        if(meal.getMealId().equals(null)) {
            throw new MealNotFoundException();
        }
        meal.validateRecipe();
        meal.validate();

        List<Recipe> existingRecipeList = getRecipesByMealId(meal.getMealId());
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

        String sql = "UPDATE meal SET name = ?, category = ?, image_file_name = ? WHERE meal_id = ?";
        jdbcTemplate.update(sql, meal.getName(), meal.getCategory(), meal.getImageFileName(),
            meal.getMealId());
        return getMealById(meal.getMealId());
    }

    @Override
    public void deleteMeal(Meal meal) throws RecipeException {
        deleteRecipesFromMeal(meal, meal.getRecipeList());

        // delete the meal plan
        String sql = "DELETE FROM meal WHERE meal_id = ?";
        jdbcTemplate.update(sql, meal.getMealId());
    }

    @Override
    public void deleteRecipesFromMeal(Meal meal, List<Recipe> recipes) throws RecipeException {
        // loop over list of ingredients
        for(Recipe recipe : recipes) {
            // check to see if the ingredient is being used in a recipe
            if(doesMealHaveRecipe(meal, recipe)) {
                // if it does delete call deleteIngredientFromRecipe
                deleteRecipeFromMeal(meal, recipe);
            }
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

    private List<Recipe> getRecipesByMealId(Long mealId) throws RecipeNotFoundException {
        List<Recipe> recipes = new ArrayList<Recipe>();
        String sql = "SELECT recipe_id " +
                "FROM recipe_meal WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId);
        while(rows.next()) {
            Long recipeId = rows.getLong("recipe_id");
            Recipe recipe = recipeDAO.getRecipeById(recipeId);
            recipes.add(recipe);
        }
        return recipes;
    }

    private Meal mapMealPlan(SqlRowSet row) {
        Meal meal = new Meal();

        meal.setMealId(row.getLong("meal_id"));
        meal.setName(row.getString("name"));
        meal.setCategory(row.getString("category"));
        meal.setImageFileName(row.getString("image_file_name"));

        return meal;
    }
}

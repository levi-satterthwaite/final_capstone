package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealPlanAlreadyExistsException;
import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.exceptions.MealPlanNotFoundException;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcMealPlanDAO implements MealPlanDAO {
    private JdbcTemplate jdbcTemplate;
    private RecipeDAO recipeDAO;

    public JdbcMealPlanDAO(JdbcTemplate jdbcTemplate, RecipeDAO recipeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.recipeDAO = recipeDAO;
    }

    @Override
    public List<MealPlan> getListOfMealPlans() throws RecipeNotFoundException {
        List<MealPlan> mealPlans = new ArrayList<MealPlan>();
        String sql = "SELECT meal_id, name, description, image_file_name FROM meal_plan";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while(rows.next()) {
            MealPlan mealPlan = mapMealPlan(rows);
            mealPlan.setRecipeList(getRecipesByMealId(mealPlan.getMealId()));
            mealPlans.add(mealPlan);
        }
        return mealPlans;
    }

    @Override
    public List<MealPlan> getMealPlansByName(String name) throws RecipeNotFoundException {
        List<MealPlan> mealPlans = new ArrayList<MealPlan>();
        String sql = "SELECT meal_id, name, description, image_file_name FROM meal_plan WHERE name ILIKE ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%");
        while(rows.next()) {
            MealPlan mealPlan = mapMealPlan(rows);
            mealPlan.setRecipeList(getRecipesByMealId(mealPlan.getMealId()));
            mealPlans.add(mealPlan);
        }
        return mealPlans;
    }

    @Override
    public MealPlan getMealPlanById(Long mealId) throws MealPlanNotFoundException, RecipeNotFoundException {
        MealPlan mealPlan = null;
        String sql = "SELECT meal_id, name, description, image_file_name FROM meal_plan WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId);
        if(rows.next()) {
            mealPlan = mapMealPlan(rows);
            mealPlan.setRecipeList(getRecipesByMealId(mealPlan.getMealId()));
        }
        if(mealPlan == null) {
            throw new MealPlanNotFoundException();
        }
        return mealPlan;
    }

    @Override
    public MealPlan addMealPlan(MealPlan mealPlan) throws MealPlanException {
        try {
            String sql = "INSERT INTO meal_plan (meal_id, name, description, image_file_name) VALUES " +
                    "(DEFAULT, ?, ?, ?) RETURNING meal_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlan.getName(), mealPlan.getDescription(),
                    mealPlan.getImageFileName());
            rows.next();
            mealPlan.setMealId(rows.getLong("meal_id"));
            return mealPlan;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new MealPlanAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }

    }

    @Override
    public MealPlan addRecipesToMealPlan(MealPlan mealPlan, List<Recipe> recipes) throws MealPlanNotFoundException,
            RecipeNotFoundException {
        for(Recipe recipe : recipes) {
            addRecipeToMealPlan(mealPlan, recipe);
        }
        return getMealPlanById(mealPlan.getMealId());
    }

    @Override
    public MealPlan updateMealPlan(MealPlan mealPlan) throws MealPlanException, RecipeNotFoundException {
        if(mealPlan.getMealId().equals(null)) {
            throw new MealPlanNotFoundException();
        }
        mealPlan.validate();
        String sql = "UPDATE meal_plan SET name = ?, description = ?, image_file_name = ? WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealPlan.getName(), mealPlan.getDescription(), mealPlan.getImageFileName(),
            mealPlan.getMealId());

        // TODO need to handle adding / removing recipes in the update

        return getMealPlanById(mealPlan.getMealId());
    }

    @Override
    public void deleteMealPlan(MealPlan mealPlan) throws RecipeException {
        deleteRecipesFromMealPlan(mealPlan, mealPlan.getRecipeList());

        // delete the meal plan
        String sql = "DELETE FROM meal_plan WHERE meal_id = ?";
        jdbcTemplate.update(sql, mealPlan.getMealId());
    }

    @Override
    public void deleteRecipesFromMealPlan(MealPlan mealPlan, List<Recipe> recipes) throws RecipeException {
        // loop over list of ingredients
        for(Recipe recipe : recipes) {
            // check to see if the ingredient is being used in a recipe
            if(doesMealPlanHaveRecipe(mealPlan, recipe)) {
                // if it does delete call deleteIngredientFromRecipe
                deleteRecipeFromMealPlan(mealPlan, recipe);
            }
        }
    }

    private boolean doesMealPlanHaveRecipe(MealPlan mealPlan, Recipe recipe) {
        // check database to see if a recipe is using the ingredient
        String sql = "SELECT meal_id, recipe_id FROM recipe_meal_plan WHERE meal_id = ? AND recipe_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlan.getMealId(), recipe.getRecipeId());
        if(rows.next()) {
            return true;
        }
        return false;
    }

    private void deleteRecipeFromMealPlan(MealPlan mealPlan, Recipe recipe) {
        // execute sql statement to remove the record from the join table
        String sql = "DELETE FROM recipe_meal_plan WHERE meal_id = ? AND recipe_id = ?";
        jdbcTemplate.update(sql, mealPlan.getMealId(), recipe.getRecipeId());
    }

    private void addRecipeToMealPlan(MealPlan mealPlan, Recipe recipe) {
        String sql = "INSERT INTO recipe_meal_plan (recipe_id, meal_id) VALUES " +
                "(?, ?) RETURNING meal_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId(), mealPlan.getMealId());
        rows.next();
    }

    private List<Recipe> getRecipesByMealId(Long mealId) throws RecipeNotFoundException {
        List<Recipe> recipes = new ArrayList<Recipe>();
        String sql = "SELECT recipe_id " +
                "FROM recipe_meal_plan WHERE meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealId);
        while(rows.next()) {
            Long recipeId = rows.getLong("recipe_id");
            Recipe recipe = recipeDAO.getRecipeById(recipeId);
            recipes.add(recipe);
        }
        return recipes;
    }

    private MealPlan mapMealPlan(SqlRowSet row) {
        MealPlan mealPlan = new MealPlan();

        mealPlan.setMealId(row.getLong("meal_id"));
        mealPlan.setName(row.getString("name"));
        mealPlan.setDescription(row.getString("description"));
        mealPlan.setImageFileName(row.getString("image_file_name"));

        return mealPlan;
    }
}

package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.*;
import com.techelevator.recipes.model.Category;
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
public class JdbcRecipeDAO implements RecipeDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcRecipeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> getListOfRecipes() {
            List<Recipe> recipes = new ArrayList<Recipe>();
            String sql = "SELECT recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                    "serving_size, instructions, date_created, image_file_name " +
                    "FROM recipe";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
            while (rows.next()) {
                Recipe recipe = mapRecipe(rows);
                recipe.setIngredientList(getIngredientsByRecipeId(recipe.getRecipeId()));
                recipes.add(recipe);
            }
            return recipes;
    }

    @Override
    public List<Recipe> getRecipesByName(String name) {
        List<Recipe> recipes = new ArrayList<Recipe>();
        String sql = "SELECT recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name " +
                "FROM recipe WHERE name ILIKE ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%");
        while(rows.next()) {
            recipes.add(mapRecipe(rows));
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long recipeId) throws RecipeNotFoundException {
            Recipe recipe = null;
            String sql = "SELECT recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                    "serving_size, instructions, date_created, image_file_name " +
                    "FROM recipe WHERE recipe_id = ?";
            SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, recipeId);
            if (rows.next()) {
                recipe = mapRecipe(rows);
            }
            if(recipe != null) {
                recipe.setIngredientList(getIngredientsByRecipeId(recipeId));
            }
            if(recipe == null) {
                throw new RecipeNotFoundException();
            }
            return recipe;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) throws NegativeValueException, RecipeException {
        try {
            recipe.validate();
            String sql = "INSERT INTO recipe (recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                    "serving_size, instructions, date_created, image_file_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "RETURNING recipe_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(
                    sql, recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                    recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(), recipe.getInstructions(),
                    recipe.getDateCreated(), recipe.getImageFileName());
            rows.next();
            recipe.setRecipeId(rows.getLong("recipe_id"));
            return recipe;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new RecipeAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public Recipe addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) throws NegativeValueException, RecipeNotFoundException {
        try {
            for(Ingredient ingredient : ingredients) {
                addIngredientToRecipe(recipe, ingredient);
            }
            return getRecipeById(recipe.getRecipeId());
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public void deleteRecipe(Recipe recipe) throws RecipeException {
        // check to see if recipe is being used in a meal plan
        makeSureRecipeIsNotInAMealPlan(recipe);
        // delete all recipe ingredients first
        deleteIngredientsFromRecipe(recipe, recipe.getIngredientList());
        // delete the recipe (use the sql statement here)
        String sql = "DELETE FROM recipe WHERE recipe_id = ?";
        jdbcTemplate.update(sql, recipe.getRecipeId());
    }

    @Override
    public void deleteIngredientsFromRecipe(Recipe recipe, List<Ingredient> ingredients) throws RecipeException {
        // loop over list of ingredients
        for(Ingredient ingredient : ingredients) {
            // check to see if the ingredient is being used in a recipe
            if(doesRecipeHaveIngredient(recipe, ingredient)) {
                // if it does delete call deleteIngredientFromRecipe
                deleteIngredientFromRecipe(recipe, ingredient);
            }
        }
    }

    private void makeSureRecipeIsNotInAMealPlan(Recipe recipe) throws RecipeException {
        // check the database to make sure there are no meal plans using this recipe
        // if there are any rows, throw an Error stating that the recipe is in use
        String sql = "SELECT recipe_id, meal_id FROM recipe_meal_plan WHERE recipe_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId());
        if(rows.next()) {
            throw new RecipeInUseException();
        }
    }

    private boolean doesRecipeHaveIngredient(Recipe recipe, Ingredient ingredient) {
        // check database to see if a recipe is using the ingredient
        String sql = "SELECT recipe_id, ingredient_id FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId(), ingredient.getIngredientId());
        if(rows.next()) {
            return true;
        }
        return false;
    }

    private void deleteIngredientFromRecipe(Recipe recipe, Ingredient ingredient) {
        // execute sql statement to remove the record from the join table
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id = ? AND ingredient_id = ?";
        jdbcTemplate.update(sql, recipe.getRecipeId(), ingredient.getIngredientId());
    }


    private void addIngredientToRecipe(Recipe recipe, Ingredient ingredient) {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, "
                + "unit_measurement) VALUES (?, ?, ?, ?) RETURNING recipe_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId(), ingredient.getIngredientId(),
                ingredient.getQuantity(), ingredient.getUnitMeasurement());
        rows.next();
    }

    private List<Ingredient> getIngredientsByRecipeId(Long recipeId)  {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        String sql = "SELECT recipe.recipe_id, ingredient.ingredient_id AS ingredient_id, ingredient.name AS ingredient_name, "
                + "ingredient.category AS ingredient_category, quantity, unit_measurement " +
                "FROM recipe " +
                "JOIN recipe_ingredient ON recipe_ingredient.recipe_id = recipe.recipe_id " +
                "JOIN ingredient ON recipe_ingredient.ingredient_id = ingredient.ingredient_id " +
                "WHERE recipe.recipe_id= ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipeId);
        while(rows.next()) {
            ingredients.add(mapRecipeIngredient(rows));
        }
        return ingredients;
    }

    private Recipe mapRecipe(SqlRowSet row) {
        Recipe recipe = new Recipe();
        recipe.setRecipeId(row.getLong("recipe_id"));
        recipe.setName(row.getString("name"));
        recipe.setCategory(row.getString("category"));
        recipe.setDifficultyLevel(row.getString("difficulty_level"));
        recipe.setPrepTimeMin(row.getInt("prep_time_min"));
        recipe.setCookTimeMin(row.getInt("cook_time_min"));
        recipe.setServingSize(row.getInt("serving_size"));
        recipe.setInstructions(row.getString("instructions"));
        recipe.setDateCreated(row.getDate("date_created").toLocalDate());
        recipe.setImageFileName(row.getString("image_file_name"));

        return recipe;
    }

    private Ingredient mapRecipeIngredient(SqlRowSet row) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(row.getLong("ingredient_id"));
        ingredient.setName(row.getString("ingredient_name"));
        ingredient.setCategory(row.getString("ingredient_category"));
        ingredient.setQuantity(row.getDouble("quantity"));
        ingredient.setUnitMeasurement(row.getString("unit_measurement"));

        return ingredient;
    }

}

package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.*;
import com.techelevator.recipes.model.Ingredient;
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
public class JdbcRecipeDAO implements RecipeDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcRecipeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> getListOfRecipes() {
            List<Recipe> recipes = new ArrayList<Recipe>();
            String sql = "SELECT recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
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
    public List<Recipe> getRecipesByName(String name, Long userId) {
        List<Recipe> recipes = new ArrayList<Recipe>();
        String sql = "SELECT recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name " +
                "FROM recipe WHERE name ILIKE ? AND user_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%", userId);
        while(rows.next()) {
            Recipe recipe = mapRecipe(rows);
            recipe.setIngredientList(getIngredientsByRecipeId(recipe.getRecipeId()));
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long recipeId, Long userId) throws RecipeNotFoundException {
            Recipe recipe = null;
            String sql = "SELECT recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                    "serving_size, instructions, date_created, image_file_name " +
                    "FROM recipe WHERE recipe_id = ? AND user_id = ?";
            SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql, recipeId, userId);
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
            String sql = "INSERT INTO recipe (recipe_id, user_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                    "serving_size, instructions, date_created, image_file_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "RETURNING recipe_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(
                    sql, recipe.getUserId(), recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                    recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(), recipe.getInstructions(),
                    recipe.getDateCreated(), recipe.getImageFileName());
            rows.next();
            recipe.setRecipeId(rows.getLong("recipe_id"));
            return recipe;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new RecipeAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public Recipe addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) throws NegativeValueException,
            RecipeNotFoundException {
        try {
            for(Ingredient ingredient : ingredients) {
                addIngredientToRecipe(recipe, ingredient);
            }
            return getRecipeById(recipe.getRecipeId(), recipe.getUserId());
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public void deleteRecipe(Recipe recipe) throws RecipeException {
        // check to see if recipe is being used in a meal plan
        makeSureRecipeIsNotInAMeal(recipe);
        // delete all recipe ingredients first
        deleteIngredientsFromRecipe(recipe, recipe.getIngredientList());
        // delete the recipe (use the sql statement here)
        String sql = "DELETE FROM recipe WHERE recipe_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, recipe.getRecipeId(), recipe.getUserId());
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

    @Override
    public Recipe updateRecipe(Recipe recipe, Long userId) throws NegativeValueException, RecipeException {
        if (recipe.getRecipeId().equals(null)) {
            throw new RecipeNotFoundException();
        }
        recipe.validate();

        // we want to get a list of the existing ingredients (recipe_ingredients) that are in database
        // and compare them to what is in the recipe object.
        List<Ingredient> existingIngredientsList = getIngredientsByRecipeId(recipe.getRecipeId());
        Map<Long, Ingredient> existingIngredientsMap = new HashMap<Long, Ingredient>();
        for(Ingredient existingIngredient : existingIngredientsList) {
            // placing into Map to make it easier to do look-ups rather than array searches
            existingIngredientsMap.put(existingIngredient.getIngredientId(), existingIngredient);
        }

        for(Ingredient recipeIngredient : recipe.getIngredientList()) {

            // if the ingredient in the recipe is not in the existing list of ingredients,
            //  we want to add it.
            if(!existingIngredientsMap.containsKey(recipeIngredient.getIngredientId())) {
                addIngredientToRecipe(recipe, recipeIngredient);
            }

            // if the ingredient in the recipe is already in the existing map of ingredients,
            //  we want to update the recipe_ingredient with the quantity and unit of measurement,
            // only if the values have changed
            else if(existingIngredientsMap.containsKey(recipeIngredient.getIngredientId())) {
                Ingredient existingIngredient = existingIngredientsMap.get(recipeIngredient.getIngredientId());
                if(!existingIngredient.equals(recipeIngredient)) {
                    updateRecipeIngredient(recipe, recipeIngredient);
                }
                // note we are removing the key from the map since we've already handle that ingredient
                // reason why is if we have any more items in our map after this for loop we are going
                // to remove them
                existingIngredientsMap.remove(recipeIngredient.getIngredientId());
            }
        }
        // if there are existing recipes that are not in the recipe's list of ingredients, we
        //  we want to remove them
        List<Ingredient> recipeIngredientsToRemove = new ArrayList<>(existingIngredientsMap.values());
        deleteIngredientsFromRecipe(recipe, recipeIngredientsToRemove);

        // finally update the recipe record in the database
        String sql = "UPDATE recipe SET name = ?, category = ?, difficulty_level = ?, " +
                "prep_time_min = ?, cook_time_min = ?, serving_size = ?, instructions = ?, date_created = ?, " +
                "image_file_name = ? WHERE recipe_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(),
                recipe.getInstructions(), recipe.getDateCreated(), recipe.getImageFileName(), recipe.getRecipeId(),
                recipe.getUserId());
        return getRecipeById(recipe.getRecipeId(), recipe.getUserId());
    }

    private void updateRecipeIngredient(Recipe recipe, Ingredient ingredient) throws NegativeValueException {
        try {
            String sql = "UPDATE recipe_ingredient SET quantity = ?, unit_measurement = ? " +
                    "WHERE recipe_id = ? AND ingredient_id = ?";
            jdbcTemplate.update(sql, ingredient.getQuantity(), ingredient.getUnitMeasurement(),
                    recipe.getRecipeId(), ingredient.getIngredientId());
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
        }
    }

    private void makeSureRecipeIsNotInAMeal(Recipe recipe) throws RecipeException {
        // check the database to make sure there are no meal plans using this recipe
        // if there are any rows, throw an Error stating that the recipe is in use
        String sql = "SELECT recipe_id, meal_id FROM recipe_meal WHERE recipe_id = ?";
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
        recipe.setUserId(row.getLong("user_id"));
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

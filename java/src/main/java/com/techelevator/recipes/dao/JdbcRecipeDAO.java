package com.techelevator.recipes.dao;

import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRecipeDAO implements RecipeDAO {

    private JdbcTemplate jdbcTemplate;
    private IngredientDAO ingredientDAO;


    public JdbcRecipeDAO(JdbcTemplate jdbcTemplate, IngredientDAO ingredientDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.ingredientDAO = ingredientDAO;
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
            recipe.setIngredientList(ingredientDAO.getIngredientsByRecipeId(recipe.getRecipeId()));
            recipes.add(recipe);
        }
        return recipes;
    }

    @Override
    public Recipe getRecipeById(Long recipeId) {
        Recipe recipe = null;
        String sql = "SELECT recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name " +
                "FROM recipe WHERE recipe_id = ?";
        SqlRowSet rows =  jdbcTemplate.queryForRowSet(sql,recipeId);
        if (rows.next()) {
            recipe = mapRecipe(rows);
        }
        if(recipe != null) {
            recipe.setIngredientList(ingredientDAO.getIngredientsByRecipeId(recipeId));
        }
        return recipe;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        String sql = "INSERT INTO recipe (recipe_id, name, category, difficulty_level, prep_time_min, cook_time_min, " +
                "serving_size, instructions, date_created, image_file_name) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING recipe_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(
                sql, recipe.getRecipeId(), recipe.getName(), recipe.getCategory(), recipe.getDifficultyLevel(),
                recipe.getPrepTimeMin(), recipe.getCookTimeMin(), recipe.getServingSize(), recipe.getInstructions(),
                recipe.getDateCreated(), recipe.getImageFileName());
        rows.next();
        recipe.setRecipeId(rows.getLong("recipe_id"));
        return recipe;
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
        recipe.setDateCreated(row.getDate("date_created"));
        recipe.setImageFileName(row.getString("image_file_name"));

        return recipe;



    }


}

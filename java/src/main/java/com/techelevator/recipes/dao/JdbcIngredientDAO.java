package com.techelevator.recipes.dao;

import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcIngredientDAO implements IngredientDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcIngredientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        return null;
    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) {
        return null;
    }

    @Override
    public Ingredient getIngredientByName(String name) {
        return null;
    }

    @Override
    public List<Ingredient> getIngredientsByRecipeId(Long recipeId) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        String sql = "SELECT recipe.recipe_id, ingredient.ingredient_id AS ingredient_id, ingredient.name AS ingredient_name, ingredient.category AS ingredient_category, quantity, unit_measurement " +
                "FROM recipe " +
                "JOIN recipe_ingredient ON recipe_ingredient.recipe_id = recipe.recipe_id " +
                "JOIN ingredient ON recipe_ingredient.ingredient_id = ingredient.ingredient_id " +
                "WHERE recipe.recipe_id= ?";

        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, recipeId);
        while (rows.next()) {
            Ingredient ingredient = mapIngredient(rows);
            ingredients.add(ingredient);

        }

        return ingredients;
    }

    private Ingredient mapIngredient(SqlRowSet row) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(row.getLong("ingredient_id"));
        ingredient.setName(row.getString("ingredient_name"));
        ingredient.setCategory(row.getString("ingredient_category"));
        ingredient.setQuantity(row.getDouble("quantity"));
        ingredient.setUnitMeasurement(row.getString("unit_measurement"));

        return ingredient;
    }
}

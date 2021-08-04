package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.model.Ingredient;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcIngredientDAO implements IngredientDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcIngredientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) throws  IngredientException {
        try {
            String sql = "INSERT INTO ingredient (ingredient_id, name, category) "
                    + "VALUES (DEFAULT, ?, ?) RETURNING ingredient_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, ingredient.getName(), ingredient.getCategory());
            rows.next();
            ingredient.setIngredientId(rows.getLong("ingredient_id"));
            return ingredient;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new IngredientException("This Ingredient Already Exists", e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) {
        Ingredient ingredient = null;
        String sql = "SELECT ingredient_id, name, category FROM ingredient WHERE ingredient_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, ingredientId);
        if(rows.next()) {
            ingredient = mapIngredient(rows);
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> getIngredientsByName(String name) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        String sql = "SELECT ingredient_id, name, category FROM ingredient WHERE name ILIKE ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%");
        while(rows.next()) {
            ingredients.add(mapIngredient(rows));
        }
        return ingredients;
    }

    @Override
    public List<Ingredient> getIngredientsByRecipeId(Long recipeId)  {
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

    private Ingredient mapIngredient(SqlRowSet row) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(row.getLong("ingredient_id"));
        ingredient.setName(row.getString("name"));
        ingredient.setCategory(row.getString("category"));

        return ingredient;
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

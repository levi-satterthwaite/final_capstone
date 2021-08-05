package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.IngredientAlreadyExistsException;
import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.IngredientNotFoundException;
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
    public Ingredient addIngredient(Ingredient ingredient) throws IngredientException {
        try {
            String sql = "INSERT INTO ingredient (ingredient_id, name, category) "
                    + "VALUES (DEFAULT, ?, ?) RETURNING ingredient_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, ingredient.getName(), ingredient.getCategory());
            rows.next();
            ingredient.setIngredientId(rows.getLong("ingredient_id"));
            return ingredient;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") && ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new IngredientAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public Ingredient getIngredientById(Long ingredientId) throws IngredientNotFoundException {
            Ingredient ingredient = null;
            String sql = "SELECT ingredient_id, name, category FROM ingredient WHERE ingredient_id = ?";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, ingredientId);
            if(rows.next()) {
                ingredient = mapIngredient(rows);
            }
            if(ingredient == null) {
                throw new IngredientNotFoundException();
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

    private Ingredient mapIngredient(SqlRowSet row) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(row.getLong("ingredient_id"));
        ingredient.setName(row.getString("name"));
        ingredient.setCategory(row.getString("category"));

        return ingredient;
    }


}

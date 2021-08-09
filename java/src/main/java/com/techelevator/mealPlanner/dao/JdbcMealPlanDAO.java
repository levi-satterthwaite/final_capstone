package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.*;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
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
public class JdbcMealPlanDAO implements MealPlanDAO {

    private JdbcTemplate jdbcTemplate;
    private MealDAO mealDAO;

    public JdbcMealPlanDAO(JdbcTemplate jdbcTemplate, MealDAO mealDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.mealDAO = mealDAO;
    }

    @Override
    public List<MealPlan> getMealPlanByName(String name, Long userId) throws MealNotFoundException, RecipeNotFoundException {
        List<MealPlan> mealPlans = new ArrayList<MealPlan>();
        String sql = "SELECT meal_plan_id, user_id, name, description, image_file_name FROM meal_plan WHERE name ILIKE ? AND user_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, "%" + name + "%", userId);
        while(rows.next()) {
            MealPlan mealPlan = mapMealPlan(rows);
            mealPlan.setMealList(getMealsByMealPlanId(mealPlan.getMealPlanId(), mealPlan.getUserId()));
            mealPlans.add(mealPlan);
        }
        return mealPlans;
    }

    @Override
    public MealPlan getMealPlanById(Long mealPlanId, Long userId) throws MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException {
        MealPlan mealPlan = null;
        String sql = "SELECT meal_plan_id, user_id, name, description, image_file_name FROM meal_plan WHERE meal_plan_id = ? AND user_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlanId, userId);
        if(rows.next()) {
            mealPlan = mapMealPlan(rows);
            mealPlan.setMealList(getMealsByMealPlanId(mealPlan.getMealPlanId(), mealPlan.getUserId()));
        }
        if(mealPlan == null) {
            throw new MealPlanNotFoundException();
        }
        return mealPlan;
    }

    @Override
    public MealPlan addMealPlan(MealPlan mealPlan) throws MealPlanException {
        try {
            String sql = "INSERT INTO meal_plan (meal_plan_id, user_id, name, description, image_file_name) VALUES " +
                    "(DEFAULT, ?, ?, ?, ?) RETURNING meal_plan_id";
            SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlan.getUserId(), mealPlan.getName(), mealPlan.getDescription(),
                    mealPlan.getImageFileName());
            rows.next();
            mealPlan.setMealPlanId(rows.getLong("meal_plan_id"));
            return mealPlan;
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23505"))
                throw new MealPlanAlreadyExistsException(e.getMostSpecificCause());
            throw e;
        }
    }

    @Override
    public MealPlan addMealsToMealPlan(MealPlan mealPlan, List<Meal> meals) throws MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException {
        for(Meal meal : meals) {
            addMealToMealPlan(mealPlan, meal);
        }
        return getMealPlanById(mealPlan.getMealPlanId(), mealPlan.getUserId());
    }

    @Override
    public MealPlan updateMealPlan(MealPlan mealPlan, Long userId) throws MealPlanNotFoundException, InvalidMealException, RecipeNotFoundException, MealNotFoundException, NegativeValueException {
        if(mealPlan.getMealPlanId().equals(null)) {
            throw new MealPlanNotFoundException();
        }
        mealPlan.validateMeal();

        List<Meal> existingMealList = getMealsByMealPlanId(mealPlan.getMealPlanId(), mealPlan.getUserId());
        Map<Long, Meal> existingMealsMap = new HashMap<Long, Meal>();
        for(Meal existingMeal : existingMealList) {
            existingMealsMap.put(existingMeal.getMealId(), existingMeal);
        }

        for(Meal mealPlanMeal : mealPlan.getMealList()) {
            if(!existingMealsMap.containsKey(mealPlanMeal.getMealId())) {
                addMealToMealPlan(mealPlan, mealPlanMeal);
            }
            else if(existingMealsMap.containsKey(mealPlanMeal.getMealId())) {
                Meal existingMeal = existingMealsMap.get(mealPlanMeal.getMealId());
                if(!existingMeal.equals(mealPlanMeal)) {
                    updateMealPlanMeal(mealPlan, mealPlanMeal);
                }
                existingMealsMap.remove(mealPlanMeal.getMealId());
            }
        }
        List<Meal> mealPlanMealsToRemove = new ArrayList<>(existingMealsMap.values());
        deleteMealsFromMealPlan(mealPlan, mealPlanMealsToRemove);

        String sql = "UPDATE meal_plan SET name = ?, description = ?, image_file_name = ? WHERE meal_plan_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, mealPlan.getName(), mealPlan.getDescription(), mealPlan.getImageFileName(),
                mealPlan.getMealPlanId(), mealPlan.getUserId());
        return getMealPlanById(mealPlan.getMealPlanId(), mealPlan.getUserId());
    }

    @Override
    public void deleteMealPlan(MealPlan mealPlan) {
        deleteMealsFromMealPlan(mealPlan, mealPlan.getMealList());

        String sql = "DELETE FROM meal_plan WHERE meal_plan_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, mealPlan.getMealPlanId(), mealPlan.getUserId());
    }

    @Override
    public void deleteMealsFromMealPlan(MealPlan mealPlan, List<Meal> meals) {
        for(Meal meal : meals) {
            if(doesMealPlanHaveMeal(mealPlan, meal)) {
                deleteMealFromMealPlan(mealPlan, meal);
            }
        }
    }

    private boolean doesMealPlanHaveMeal(MealPlan mealPlan, Meal meal) {
        String sql = "SELECT meal_plan_id, meal_id FROM meal_plan_meal WHERE meal_plan_id = ? AND meal_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlan.getMealPlanId(), meal.getMealId());
        if(rows.next()) {
            return true;
        }
        return false;
    }

    private void deleteMealFromMealPlan(MealPlan mealPlan, Meal meal) {
        String sql = "DELETE FROM meal_plan_meal WHERE meal_plan_id = ? AND meal_id = ?";
        jdbcTemplate.update(sql, mealPlan.getMealPlanId(), meal.getMealId());
    }

    private void updateMealPlanMeal(MealPlan mealPlan, Meal meal) throws NegativeValueException {
        try {
            String sql = "UPDATE meal_plan_meal WHERE meal_plan_id = ? AND meal_id = ?";
            jdbcTemplate.update(sql, mealPlan.getMealPlanId(), meal.getMealId());
        } catch(DataIntegrityViolationException e) {
            if (e.getMostSpecificCause().getClass().getName().equals("org.postgresql.util.PSQLException") &&
                    ((SQLException) e.getMostSpecificCause()).getSQLState().equals("23514"))
                throw new NegativeValueException("Negative Value Not Allowed", e.getMostSpecificCause());
        }
    }

    private void addMealToMealPlan(MealPlan mealPlan, Meal meal) {
        String sql = "INSERT INTO meal_plan_meal (meal_plan_id, meal_id) VALUES " +
                "(?, ?) RETURNING meal_plan_id";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlan.getMealPlanId(), meal.getMealId());
        rows.next();
    }

    private List<Meal> getMealsByMealPlanId(Long mealPlanId, Long userId) throws MealNotFoundException, RecipeNotFoundException {
        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal_id " +
                "FROM meal_plan_meal WHERE meal_plan_id = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, mealPlanId);
        while(rows.next()) {
            Long mealId = rows.getLong("meal_id");
            Meal meal = mealDAO.getMealById(mealId, userId);
            meals.add(meal);
        }
        return meals;
    }

    private MealPlan mapMealPlan(SqlRowSet row) {
        MealPlan mealPlan = new MealPlan();

        mealPlan.setMealPlanId(row.getLong("meal_plan_id"));
        mealPlan.setUserId(row.getLong("user_id"));
        mealPlan.setName(row.getString("name"));
        mealPlan.setDescription(row.getString("description"));
        mealPlan.setImageFileName(row.getString("image_file_name"));

        return mealPlan;
    }
}

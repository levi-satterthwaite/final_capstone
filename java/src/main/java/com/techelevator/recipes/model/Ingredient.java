package com.techelevator.recipes.model;

import java.util.Objects;

public class Ingredient {

    private Long ingredientId;
    private String name;
    private String category;
    private double quantity;
    private String unitMeasurement;

    public Ingredient() {

    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(that.quantity, quantity) == 0 && Objects.equals(ingredientId, that.ingredientId) && Objects.equals(name, that.name) && Objects.equals(category, that.category) && Objects.equals(unitMeasurement, that.unitMeasurement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, name, category, quantity, unitMeasurement);
    }
}

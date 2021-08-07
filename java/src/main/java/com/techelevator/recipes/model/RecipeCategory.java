package com.techelevator.recipes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeCategory {

    public static final RecipeCategory APPETIZER = new RecipeCategory("Appetizer");
    public static final RecipeCategory ENTREE = new RecipeCategory("Entree");
    public static final RecipeCategory SIDE_DISH = new RecipeCategory("Side Dish");
    public static final RecipeCategory SOUP = new RecipeCategory("Soup");
    public static final RecipeCategory SALAD = new RecipeCategory("Salad");
    public static final RecipeCategory DESSERT = new RecipeCategory("Dessert");
    public static final RecipeCategory BEVERAGE = new RecipeCategory("Beverage");
    public static final RecipeCategory COCKTAIL = new RecipeCategory("Cocktail");

    private String name;

    public RecipeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<RecipeCategory> getCategories() {
        List<RecipeCategory> categories = new ArrayList<RecipeCategory>();
        categories.add(RecipeCategory.APPETIZER);
        categories.add(RecipeCategory.ENTREE);
        categories.add(RecipeCategory.SIDE_DISH);
        categories.add(RecipeCategory.SOUP);
        categories.add(RecipeCategory.SALAD);
        categories.add(RecipeCategory.DESSERT);
        categories.add(RecipeCategory.BEVERAGE);
        categories.add(RecipeCategory.COCKTAIL);
        return categories;
    }

    public static boolean isValidCategory(String categoryName) {
        return getCategories().indexOf(new RecipeCategory(categoryName)) >= 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeCategory recipeCategory = (RecipeCategory) o;
        return Objects.equals(name, recipeCategory.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

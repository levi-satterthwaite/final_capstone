package com.techelevator.recipes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {

    public static final Category APPETIZER = new Category("Appetizer");
    public static final Category ENTREE = new Category("Entree");
    public static final Category SIDE_DISH = new Category("Side Dish");
    public static final Category SOUP = new Category("Soup");
    public static final Category SALAD = new Category("Salad");
    public static final Category DESSERT = new Category("Dessert");
    public static final Category SNACK = new Category("Snack");
    public static final Category BEVERAGE = new Category("Beverage");
    public static final Category COCKTAIL = new Category("Cocktail");

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        categories.add(Category.APPETIZER);
        categories.add(Category.ENTREE);
        categories.add(Category.SIDE_DISH);
        categories.add(Category.SOUP);
        categories.add(Category.SALAD);
        categories.add(Category.DESSERT);
        categories.add(Category.SNACK);
        categories.add(Category.BEVERAGE);
        categories.add(Category.COCKTAIL);
        return categories;
    }

    public static boolean isValidCategory(String categoryName) {
        return getCategories().indexOf(new Category(categoryName)) >= 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

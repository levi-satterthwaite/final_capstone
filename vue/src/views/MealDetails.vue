<template>
  <div class="page meal-details">
    <h1 class="meal-name-category">{{ meal.name }} | {{ meal.category }}</h1>
    <div class="recipes-by-category" v-if="meal">
      <div
        class="recipe-category"
        v-for="category in mealRecipesByCategory.keys()"
        v-bind:key="category"
      >
        <h3>
          {{ category }}
        </h3>
        <div class="recipe-list">
          <recipe-card
            v-for="recipe in mealRecipesByCategory.get(category)"
            v-bind:key="recipe.id"
            v-bind:recipe="recipe"
          />
        </div>
      </div>
    </div>
    <p v-else>Meal not found!</p>
    <div class="action-bar">
      <router-link
        v-bind:to="{ name: 'updateMeal', params: { id: meal.mealId } }"
        tag="button"
        class="btn"
        >Edit Meal</router-link
      >
    </div>
  </div>
</template>

<script>
// import MealCard from "@/components/MealCard";
import RecipeCard from "@/components/RecipeCard"

export default {
  name: "meal-details",
  data() {
    return {
      meal: null,
    };
  },
  components: {
    // MealCard,
    RecipeCard
  },
  computed: {
    mealRecipesByCategory() {
      // key is the category name and the value is a list of recipes
      const recipesByCategory = new Map();
      // for each loop
      for (const recipe of this.meal.recipeList) {
        if (!recipesByCategory.has(recipe.category)) {
          recipesByCategory.set(recipe.category, []);
        }
        recipesByCategory.get(recipe.category).push(recipe);
      }
      return recipesByCategory;
    },
  },
  created() {
    const mealId = this.$route.params.id;
    this.meal = this.$store.state.meals.find((meal) => {
      return meal.mealId == mealId;
    });
  },
};
</script>

<style>
div.meal-details h1.meal-name-category {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 50px;
  color: #4b3f72;
}
div.meal-details {
  display: flex;
  flex-direction: column;
}
div.recipe-category h3 {
  text-align: center;
  font-size: 16pt;
   color: #4b3f72;
}
</style>
<template>
  <div class="page meal-details">
    <div class="error" v-if="error">{{ error.message }}</div>
    <h1 class="meal-name-category" v-if="meal">
      {{ meal.name }} | {{ meal.category }}
    </h1>
    <div class="recipes-by-category" v-if="meal">
      <p  v-if="!hasRecipes">Add some recipes to your meal.</p>
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
            v-bind:key="recipe.recipeId"
            v-bind:recipe="recipe"
          />
        </div>
      </div>
    </div>
    <p v-else>Meal not found!</p>
    <div class="action-bar" v-if="meal">
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
import mealPlannerService from "@/services/MealPlannerService"
import RecipeCard from "@/components/RecipeCard";

export default {
  name: "meal-details",
  data() {
    return {
      meal: null,
      error: null
    };
  },
  components: {
    RecipeCard,
  },
  computed: {
    hasRecipes() {
      return Array.from(this.mealRecipesByCategory.keys()).length;
    },
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
  async created() {
    const mealId = this.$route.params.id;
    this.meal = this.$store.state.meals.find((meal) => {
      return meal.mealId == mealId;
    });
    if(!this.meal) {
      try {
        const response = await mealPlannerService.getMealById(mealId);
        this.meal = response.data;
      } catch(e) {
        this.error = mealPlannerService.getError(e);
      }
    }
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
  font-size: 20pt;
  color: #4b3f72;
}
</style>
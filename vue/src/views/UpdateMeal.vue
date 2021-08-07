<template>
  <div class="update-meal">
    <h1>Update Meal</h1>
    <p v-if="!meal">Meal not found!</p>
    <meal-form
      v-else
      v-on:submit="handleMealFormSubmit"
      v-bind:error="submitMealError"
      v-bind:data="meal"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealForm from "@/components/MealForm";

export default {
  name: "update-meal",
  data() {
    return {
      submitMealError: null,
      meal: null,
    };
  },
  components: {
    MealForm,
  },
  methods: {
    async handleMealFormSubmit({ meal, recipes, file }) {
      try {
        if (file) {
          const outputFileName = await this.addImage(file);
          meal.imageFileName = outputFileName;
        }
        meal.recipeList = recipes;
        await this.updateMeal(meal);
        this.$router.push({
          name: "mealDetails",
          params: { id: meal.mealId },
        });
      } catch (e) {
        this.submitMealError = mealPlannerService.getError(e);
      }
    },
    // Error handling is done in handleRecipeFormSubmit
    async addImage(file) {
      console.log(file);
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async updateMeal(meal) {
      const response = await mealPlannerService.updateMeal(meal);
      return response.data;
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
</style>
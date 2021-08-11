<template>
  <div class="page add-meal">
    <h1>Create Meal</h1>
    <meal-form
      v-on:submit="handleMealFormSubmit"
      v-bind:error="submitMealError"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealForm from "@/components/MealForm";

export default {
  name: "add-meal",
  data() {
    return {
      submitMealError: null,
    };
  },
  components: {
    MealForm,
  },
  methods: {
    async handleMealFormSubmit({ meal, recipes, file }) {
      try {
        const outputFileName = await this.addImage(file);
        meal.imageFileName = outputFileName;
        const savedMeal = await this.addMeal(meal);
        await this.addRecipesToMeal(savedMeal, recipes);
        this.$router.push({ name: "meals" });
      } catch (e) {
        this.submitMealError = mealPlannerService.getError(e);
        console.error(e);
      }
    },
    async addImage(file) {
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async addMeal(meal) {
      const response = await mealPlannerService.addMeal(meal);
      return response.data;
    },
    async addRecipesToMeal(meal, recipes) {
      const response = await mealPlannerService.addRecipesToMeal(
        meal,
        recipes
      );
      return response.data;
    },
  },
};
</script>

<style>
div.add-meal {
  display: flex;
  flex-direction: column;
  align-items: center;
}

div.add-meal h1 {
  color: #4B3F72;
}
</style>
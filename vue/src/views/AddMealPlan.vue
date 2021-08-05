<template>
  <div class="add-meal-plan">
    <h1>Create Meal Plan</h1>
    <meal-plan-form
      v-on:submit="handleMealPlanFormSubmit"
      v-bind:error="submitMealPlanError"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealPlanForm from "@/components/MealPlanForm";

export default {
  name: "add-meal-plan",
  data() {
    return {
      submitMealPlanError: null,
    };
  },
  components: {
    MealPlanForm,
  },
  methods: {
    async handleMealPlanFormSubmit({ mealPlan, recipes, file }) {
      try {
        const outputFileName = await this.addImage(file);
        mealPlan.imageFileName = outputFileName;
        const savedMealPlan = await this.addMealPlan(mealPlan);
        await this.addMealPlanRecipes(savedMealPlan, recipes);
        this.$router.push({ name: "meal-plans" });
      } catch (e) {
        this.submitMealPlanError = mealPlannerService.getError(e);
        console.error(e);
      }
    },
    async addImage(file) {
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async addMealPlan(mealPlan) {
      const response = await mealPlannerService.addMealPlan(mealPlan);
      return response.data;
    },
    async addMealPlanRecipes(mealPlan, recipes) {
      const response = await mealPlannerService.addMealPlanRecipes(
        mealPlan,
        recipes
      );
      return response.data;
    },
  },
};
</script>

<style>
div.add-recipe {
  display: flex;
  flex-direction: column;
  align-items: center;
}

h1 {
  color: #4B3F72;
  margin: 50px;
}
</style>
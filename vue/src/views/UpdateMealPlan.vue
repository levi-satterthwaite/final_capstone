<template>
  <div class="update-meal-plan">
    <h1>Update Meal Plan</h1>
    <p v-if="!mealPlan">Meal plan not found!</p>
    <meal-plan-form
      v-else
      v-on:submit="handleMealPlanFormSubmit"
      v-bind:error="submitMealPlanError"
      v-bind:data="mealPlan"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealPlanForm from "@/components/MealPlanForm";

export default {
  name: "update-meal-plan",
  data() {
    return {
      submitMealPlanError: null,
      mealPlan: null,
    };
  },
  components: {
    MealPlanForm,
  },
  methods: {
    async handleMealPlanFormSubmit({ mealPlan, meals, file }) {
      try {
        if (file) {
          const outputFileName = await this.addImage(file);
          mealPlan.imageFileName = outputFileName;
        }
        mealPlan.mealList = meals;
        await this.updateMealPlan(mealPlan);
        this.$router.push({
          name: "mealPlanDetails",
          params: { id: mealPlan.mealPlanId },
        });
      } catch (e) {
        this.submitMealPlanError = mealPlannerService.getError(e);
      }
    },
    // Error handling is done in handleRecipeFormSubmit
    async addImage(file) {
      console.log(file);
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async updateMealPlan(mealPlan) {
      const response = await mealPlannerService.updateMealPlan(mealPlan);
      return response.data;
    },
  },
  created() {
    const mealPlanId = this.$route.params.id;
    this.mealPlan = this.$store.state.mealPlans.find((mealPlan) => {
      return mealPlan.mealPlanId == mealPlanId;
    });
  },
};
</script>

<style>
</style>
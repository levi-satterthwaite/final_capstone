<template>
  <div class="page meal-plans-page">
    <h1>Meal Plans</h1>
    <div class="action-bar">
      <router-link to="/mealplans/add" tag="button" class="btn">
        Add Meal Plan</router-link
      >
    </div>
    <div class="container">
      <meal-plan-list />
    </div>
  </div>
</template>

<script>
import MealPlanList from "@/components/MealPlanList";

export default {
  name: "meal-plans",
  components: {
    MealPlanList,
  },
};
</script>

<style>
</style>
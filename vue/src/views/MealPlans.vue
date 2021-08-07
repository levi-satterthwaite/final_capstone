<template>
  <div class="meal-plans-page">
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
div.meal-plans h1 {
  text-align: center;
  margin-top: 50px;
}

div.meal-plans-page {
  display: flex;
  flex-direction: column;
}

div.meal-plans-page div.container {
  display: flex;
  justify-items: center;
  margin-left: 30px;
  margin-right: 30px;
}
div.action-bar {
  display: flex;
  justify-content: flex-end;
}
</style>
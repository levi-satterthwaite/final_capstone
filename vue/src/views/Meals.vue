<template>
  <div class="page meal-page">
    <h1>Meals</h1>
    <div class="action-bar">
      <router-link to="/meals/add" tag="button" class="btn">
        Add Meal</router-link
      >
    </div>
    <div class="container">
      <meal-list />
    </div>
  </div>
</template>

<script>
import MealList from "@/components/MealList";

export default {
  name: "meals",
  components: {
    MealList,
  },
};
</script>

<style>

</style>
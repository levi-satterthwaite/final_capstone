<template>
  <div class="page grocery-list">
    <h1>Grocery List</h1>
    <meal-plan-groceries />
  </div>
</template>

<script>
// import mealPlannerService from "@/services/MealPlannerService";
import MealPlanGroceries from "@/components/MealPlanGroceries";

export default {
  name: "grocery-list",
  data() {
    return {

    }
  },
  components: {
    MealPlanGroceries
  }

}
</script>

<style>
</style>
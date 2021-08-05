<template>
  <div class="meal-plan-list">
    <meal-plan-card
      v-for="mealPlan in $store.state.mealPlans"
      v-bind:key="mealPlan.id"
      v-bind:mealPlan="mealPlan"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealPlanCard from "@/components/MealPlanCard";

export default {
  name: "meal-plan-list",
  components: {
    MealPlanCard,
  },
  methods: {
    getAllMealPlans() {
      mealPlannerService
        .getMealPlans()
        .then((response) => {
          this.$store.commit("SET_MEAL_PLANS", response.data);
        })
        .catch((error) => {
          if (error.response) {
            this.errorMsg =
              "Error Status " +
              error.response.status +
              ": " +
              error.response.statusText +
              ".";
          } else if (error.request) {
            this.errorMsg = "Server did not respond.";
          } else {
            this.errorMsg = "Something did went wrong!";
          }
        });
    },
  },
  created() {
    this.getAllMealPlans();
  }
};
</script>

<style>
div.meal-plan-list {
    display:flex;
    flex-wrap: wrap;
    justify-content: center;
}
</style>
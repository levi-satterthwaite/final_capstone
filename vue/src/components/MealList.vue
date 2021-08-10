<template>
  <div class="meal-list">
    <div class="meal-card-wrapper" v-if="$store.state.meals.length">
      <meal-card
        v-for="meal in $store.state.meals"
        v-bind:key="meal.id"
        v-bind:meal="meal"
      />
    </div>
    <p v-else>No meals have been created, please add one.</p>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealCard from "@/components/MealCard";

export default {
  name: "meal-list",
  components: {
    MealCard,
  },
  methods: {
    getAllmeals() {
      mealPlannerService
        .getMeals()
        .then((response) => {
          this.$store.commit("SET_MEALS", response.data);
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
    this.getAllmeals();
  },
};
</script>

<style>
div.meal-list, div.meal-card-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
</style>
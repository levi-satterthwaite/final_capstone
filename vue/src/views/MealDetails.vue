<template>
  <div class="meal-details">
    <h1>{{meal.name}}</h1>
    <meal-card v-if="meal" v-bind:meal="meal" />
    <p v-else>Meal not found!</p>
       <div class="action-bar">
          <router-link v-bind:to="{name: 'updateMeal', params: { id: meal.mealId} }" tag="button" class="btn">Edit Meal</router-link>
      </div>
  </div>
</template>

<script>
import MealCard from "@/components/MealCard";

export default {
  name: "meal-details",
  data() {
    return {
      meal: null
    }
  },
  components: {
    MealCard
  },
  created () {
    const mealId = this.$route.params.id;
    this.meal = this.$store.state.meals.find((meal) => {
      return meal.mealId == mealId;
    });
  }
}
</script>

<style>

</style>
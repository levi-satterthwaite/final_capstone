<template>
  <div class="meal-plan-details">
    <h1>{{mealPlan.name}}</h1>
    <meal-plan-card v-if="mealPlan" v-bind:mealPlan="mealPlan" />
    <p v-else>Meal plan not found!</p>
       <div class="action-bar">
          <router-link v-bind:to="{name: 'updateMealPlan', params: { id: mealPlan.mealPlanId} }" tag="button" class="btn">Edit Meal Plan</router-link>
      </div>
  </div>
</template>

<script>
import MealPlanCard from "@/components/MealPlanCard";

export default {
  name: "meal-plan-details",
  data() {
    return {
      mealPlan: null
    }
  },
  components: {
    MealPlanCard
  },
  created () {
    const mealPlanId = this.$route.params.id;
    this.mealPlan = this.$store.state.mealPlans.find((mealPlan) => {
      return mealPlan.mealPlanId == mealPlanId;
    });
  }
}
</script>

<style>
div.meal-plan-details h1 {
  text-align: center;
  margin-top: 50px;
}

</style>
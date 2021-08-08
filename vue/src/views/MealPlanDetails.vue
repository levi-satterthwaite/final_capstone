<template>
  <div class="page meal-plan-details">
    <h1>{{ mealPlan.name }}</h1>
    <div class="meals-by-category" v-if="mealPlan">
      <div
        class="meal-category"
        v-for="category in mealPlanMealsByCategory.keys()"
        v-bind:key="category"
      >
        <h3>
          {{ category }}
        </h3>
        <div class="meal-list">
          <meal-card 
            v-for="meal in mealPlanMealsByCategory.get(category)" 
            v-bind:key="meal.mealId"
            v-bind:meal="meal" />
        </div>
      </div>
    </div>
    <p v-else>Meal plan not found!</p>
    <div class="action-bar">
      <router-link
        v-bind:to="{
          name: 'updateMealPlan',
          params: { id: mealPlan.mealPlanId },
        }"
        tag="button"
        class="btn"
        >Edit Meal Plan</router-link
      >
    </div>
  </div>
</template>

<script>
import MealCard from "@/components/MealCard";

export default {
  name: "meal-plan-details",
  data() {
    return {
      mealPlan: null,
    };
  },
  components: {
    MealCard,
  },
  computed: {
    mealPlanMealsByCategory() {
      const mealsByCategory = new Map();
      for(const meal of this.mealPlan.mealList) {
        if(!mealsByCategory.has(meal.category)) {
          mealsByCategory.set(meal.category, []);
        }
        mealsByCategory.get(meal.category).push(meal);
      }
      return mealsByCategory;
    }
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
div.meal-plan-details h1 {
  text-align: center;
  margin-top: 50px;
  margin-bottom: 50px;
  color: #4b3f72;
}

div.meal-plan-details {
  display: flex;
  flex-direction: column;
}
div.meal-category h3 {
  text-align: center;
  font-size: 20pt;
   color: #4b3f72;
}
</style>
<template>
  <div class="page meal-plan-details">
    <div class="error" v-if="error">{{ error.message }}</div>
    <h1 v-if="mealPlan">{{ mealPlan.name }}</h1>
    <p class="meal-plan-description" v-if="mealPlan && mealPlan.description">
      {{ mealPlan.description }}
    </p>
    <div class="meals-by-category" v-if="mealPlan">
      <p v-if="!hasMeals">Add some meals to your meal plan.</p>
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
            v-bind:meal="meal"
          />
        </div>
      </div>
    </div>
    <p v-else>Meal plan not found!</p>
    <div class="action-bar" v-if="mealPlan">
      <router-link
        v-bind:to="{
          name: 'groceryList',
          params: { id: mealPlan.mealPlanId },
        }"
        tag="button"
        class="btn"
        >Create Grocery List</router-link
      >
      <router-link
        v-bind:to="{
          name: 'updateMealPlan',
          params: { id: mealPlan.mealPlanId },
        }"
        tag="button"
        class="btn"
        >Edit Meal Plan</router-link
      >
      <button class="btn" v-on:click.prevent="deleteMealPlan">Delete</button>
    </div>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealCard from "@/components/MealCard";

export default {
  name: "meal-plan-details",
  data() {
    return {
      mealPlan: null,
      error: null,
    };
  },
  components: {
    MealCard,
  },
  computed: {
    hasMeals() {
      // Array.from takes an iterator (keys()) and turns it into a list
      return Array.from(this.mealPlanMealsByCategory.keys()).length;
    },
    mealPlanMealsByCategory() {
      const mealsByCategory = new Map();
      for (const meal of this.mealPlan.mealList) {
        if (!mealsByCategory.has(meal.category)) {
          mealsByCategory.set(meal.category, []);
        }
        mealsByCategory.get(meal.category).push(meal);
      }
      return mealsByCategory;
    },
  },
  methods: {
    async deleteMealPlan() {
      try {
        if (window.confirm("Are you sure you want to delete this meal plan?")) {
          await mealPlannerService.deleteMealPlan(
            this.mealPlan
          );
          this.$router.push({name: 'mealPlans', params: {}});
        }
      } catch(e) {
        this.error = mealPlannerService.getError(e);
      }
    },
  },
  async created() {
    const mealPlanId = this.$route.params.id;
    this.mealPlan = this.$store.state.mealPlans.find((mealPlan) => {
      return mealPlan.mealPlanId == mealPlanId;
    });
    if (!this.mealPlan) {
      try {
        const response = await mealPlannerService.getMealPlanById(mealPlanId);
        this.mealPlan = response.data;
      } catch (e) {
        this.error = mealPlannerService.getError(e);
      }
    }
  },
};
</script>

<style>
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
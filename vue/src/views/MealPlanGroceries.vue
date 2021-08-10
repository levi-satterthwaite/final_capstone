<template>
  <div class="page grocery-list card">
    <h1>Grocery List</h1>
    <ul class="groceries">
      <li
        v-for="ingredient in allIngredients"
        v-bind:key="ingredient.ingredientId"
      >
        <div class="ingredient-name">
          {{ ingredient.name }}
        </div>
        <div class="ingredient-quantities">
          <div
            class="ingredient-quantity"
            v-for="quantity in ingredient.quantities"
            v-bind:key="
              ingredient.name + quantity.quantity + quantity.unitMeasurement
            "
          >
            <span class="ingredient-amount">{{ quantity.quantity }}</span>
            <span class="ingredient-unit-measurement">{{
              quantity.unitMeasurement
            }}</span>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>
<script>
import mealPlannerService from "@/services/MealPlannerService";
export default {
  name: "meal-plan-groceries",
  data() {
    return {
      mealPlan: null,
      error: null,
    };
  },
  computed: {
    allIngredients() {
      const ingredients = [];
      if (!this.mealPlan) {
        return [];
      }
      for (const meal of this.mealPlan.mealList) {
        for (const recipe of meal.recipeList) {
          for (const ingredient of recipe.ingredientList) {
            ingredients.push(ingredient);
          }
        }
      }
      const groceryList = new Map();
      for (const ingredient of ingredients) {
        if (!groceryList.has(ingredient.ingredientId)) {
          groceryList.set(ingredient.ingredientId, {
            name: ingredient.name,
            category: ingredient.category,
            quantities: [],
          });
        }
        groceryList.get(ingredient.ingredientId).quantities.push({
          quantity: ingredient.quantity,
          unitMeasurement: ingredient.unitMeasurement,
        });
      }
      console.log(Array.from(groceryList.values()));
      return Array.from(groceryList.values()).sort((a, b) => {
        if (a.category > b.category) {
          return 1;
        } else if (a.category === b.category) {
          if (a.name > b.name) {
            return 1;
          } else {
            return -1;
          }
        } else {
          return -1;
        }
      });
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
</style>
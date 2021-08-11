<template>
  <div class="card meal-plan-grocery-list">
    <div class="error" v-if="error">{{ error.message }}</div>
    <div
      class="ingredient-category"
      v-for="category in ingredientsByCategory.keys()"
      v-bind:key="category"
    >
      <h3>{{ category }}</h3>
      <div class="ingredient-list">
        <ul class="groceries">
          <li
            v-for="ingredient in ingredientsByCategory.get(category)"
            v-bind:key="ingredient.ingredientId"
            v-bind:ingredient="ingredient"
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
                <span class="ingredient-amount">{{ quantity.quantity }}</span>&nbsp;
                <span class="ingredient-unit-measurement">{{
                  quantity.unitMeasurement
                }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
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
    ingredientsByCategory() {
      const ingredientsByCategory = new Map();
      for (const ingredient of this.allIngredients) {
        if (!ingredientsByCategory.has(ingredient.category)) {
          ingredientsByCategory.set(ingredient.category, []);
        }
        ingredientsByCategory.get(ingredient.category).push(ingredient);
      }
      return ingredientsByCategory;
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
div.meal-plan-grocery-list {
  width: 50%;
  background-color: white;
  padding: 25px;
}
div.ingredient-category h3 {
  font-size: 20px;
  /* text-align: center; */
  background-color: #9d7dde;
  color: white;
  padding-left: 30px;
}
div.ingredient-category ul.groceries {
  font-size: 17px;
  color: #4b3f72;
  margin-left: 10px;
}
div.ingredient-category ul.groceries li {
  padding: 10px;
  list-style-type: none;
}
div.ingredient-category ul.groceries li div.ingredient-name {
  font-weight: bold;
}
div.ingredient-category ul.groceries li div.ingredient-quantities {
  margin-left: 30px;
}
</style>
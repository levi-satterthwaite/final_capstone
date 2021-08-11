<template>
  <div class="page grocery-list">
    <h1>Grocery List for {{mealPlan.name}} Meal Plan</h1>
    <contact-form
      v-if="isContactFormOpen"
      v-bind:message="shareMessage"
      v-on:success="handleSuccess"
    ></contact-form>
     <div class="success" v-if="isSuccess">
      You Grocery List Has Been Emailed!
    </div>
    <meal-plan-groceries v-if="ingredientsByCategory && !isContactFormOpen"
      v-bind:error="error" v-bind:ingredientsByCategory="ingredientsByCategory" />
    <div class="action-bar">
      <!-- <router-link
        v-bind:to="{ name: 'contactForm', params: {} }"
        tag="button"
        class="btn"
        >Share Grocery List</router-link
      > -->
      <button class="btn" v-if="!isContactFormOpen" v-on:click.prevent="showContactForm">Share Grocery List</button>
      <button class="btn" v-if="isContactFormOpen" v-on:click.prevent="hideContactForm">Cancel</button>
    </div>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import MealPlanGroceries from "@/components/MealPlanGroceries";
import ContactForm from "@/components/ContactForm";

export default {
  name: "grocery-list",
  data() {
    return {
      isContactFormOpen: false,
      mealPlan: null,
      error: null,
      isSuccess: false
    };
  },
  components: {
    MealPlanGroceries,
    ContactForm,
  },
  methods: {
    hideContactForm() {
      this.isContactFormOpen = false;
    } ,
    handleSuccess() {
      this.hideContactForm();
      this.isSuccess = true;
    },
    showContactForm() {
      this.isContactFormOpen = true;
    }
  },
  computed: {
    shareMessage() {
      let message = "<p>";
      const ingredientsByCategory = this.ingredientsByCategory;
      for(const category of ingredientsByCategory.keys()) {
        message += category.bold() + "<br>";
        for(const ingredient of ingredientsByCategory.get(category)) {
          message += "&emsp;" + ingredient.name + "<br>";
          for(const quantity of ingredient.quantities) {
            message += "&emsp;&emsp;" + quantity.quantity + " " + quantity.unitMeasurement + "<br>";
          } 
          message += "<br>";
        }
      }
      message += "</p>"
      return message;
    },
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
div.success {
  background-color: #9d7dde;
  color: white;
  font-size: 20px;
  text-align: center;
  width: 50%;
  border-radius: 10px;
  margin-bottom: 20px;
}
</style>
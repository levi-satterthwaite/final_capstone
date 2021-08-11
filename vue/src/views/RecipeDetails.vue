<template>
  <div class="page recipe-details">
    <div class="error" v-if="error">{{ error.message }}</div>
    <div class="recipe-details-container card">
      <img v-if="imageFileName" v-bind:src="imageFileName" />
      <h1>{{ recipe.name }}</h1>
      <div class="recipe-overview">
        <div class="recipe-overview-item">
          <span>Category</span>
          <span>{{ recipe.category }}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Difficulty Level</span>
          <span>{{ recipe.difficultyLevel }}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Serving Size</span>
          <span>{{ recipe.servingSize }}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Total Prep Time (minutes)</span>
          <span>{{ recipe.prepTimeMin }}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Total Cook Time (minutes)</span>
          <span>{{ recipe.cookTimeMin }}</span>
        </div>
        <div class="recipe-ingredients">
          <div class="recipe-overview-item">
            <span>Ingredients</span>
          </div>
          <ul class="recipe-overview-item">
            <li
              v-for="ingredient in recipe.ingredientList"
              v-bind:key="ingredient.ingredientId"
            >
              <div class="recipe-overview-item">
                {{ ingredient.name }} - {{ ingredient.quantity }}
                {{ ingredient.unitMeasurement }}
              </div>
            </li>
          </ul>
        </div>
        <div class="recipe-instructions">
          <div class="recipe-overview-item">
            <span>Instructions</span>
          </div>
          <div class="recipe-overview-item">
            <p v-for="(instruction, index) in instructions" v-bind:key="index">
              {{ instruction }}
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="action-bar" v-if="recipe">
      <router-link
        v-bind:to="{ name: 'updateRecipe', params: { id: recipe.recipeId } }"
        tag="button"
        class="btn"
        >Edit Recipe</router-link
      >
    </div>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";

export default {
  name: "recipe-detail",
  data() {
    return {
      recipe: null,
      error: null,
    };
  },
  computed: {
    imageFileName() {
      if (!this.recipe) {
        return null;
      }
      return "/files/" + this.recipe.imageFileName;
    },
    instructions() {
      if (!this.recipe) {
        return null;
      }
      return this.recipe.instructions.split("\n");
    },
  },
  async created() {
    const recipeId = this.$route.params.id;
    this.recipe = this.$store.state.recipes.find((recipe) => {
      return recipe.recipeId == recipeId;
    });
    if (!this.recipe) {
      try {
        const response = await mealPlannerService.getRecipeById(recipeId);
        this.recipe = response.data;
      } catch (e) {
        this.error = mealPlannerService.getError(e);
      }
    }
  },
};
</script>

<style>
div.recipe-details {
  display: flex;
  flex-grow: 1;
}
div.recipe-details-container {
  display: flex;
  flex-grow: 1;
  flex-direction: column;
  width: 70%;
  margin-top: 25px;
}
div.recipe-details-container img {
  border-top-right-radius: 10px;
  border-top-left-radius: 10px;
  object-fit: cover;
  width: 100%;
  max-height: 500px;
}
div.recipe-overview {
  padding: 40px;
  padding-top: 0px;
}
div.recipe-overview-item {
  padding: 3px;
  font-size: 1.25em;
  color: #4b3f72;
}
div.recipe-overview-item span:first-child {
  font-weight: bold;
  padding-right: 10px;
}
div.recipe-overview-item span:first-child::after {
  content: ":";
}
div.recipe-overview div.recipe-ingredients,
div.recipe-overview div.recipe-instructions {
  padding-top: 25px;
}

div.recipe-instructions p:first-child {
  margin-top: 0;
}

ul.recipe-overview-item {
  list-style: none;
}

ul.recipe-overview-item li:before {
  content: "\2022";  /* Add content: \2022 is the CSS Code/unicode for a bullet */
  color: #4b3f72; /* Change the color */
  font-weight: bold; /* If you want it to be bold */
  display: inline-block; /* Needed to add space between the bullet and the text */
  width: 1em; /* Also needed for space (tweak if needed) */
  margin-left: -1em; /* Also needed for space (tweak if needed) */
}

ul.recipe-overview-item li div.recipe-overview-item {
  display: inline-block;
} 
</style>
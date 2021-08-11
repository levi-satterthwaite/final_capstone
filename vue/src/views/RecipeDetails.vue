<template>
  <div class="page recipe-details">
    <div class="error" v-if="error">{{ error.message }}</div>
    <div class="recipe-details-container card">
      <img v-if="imageFileName" v-bind:src="imageFileName" />
      <h1>{{ recipe.name }}</h1>
      <div class="recipe-overview">
        <div class="recipe-overview-item">
          <span>Category</span>
          <span>{{recipe.category}}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Difficulty Level</span>
          <span>{{recipe.difficultyLevel}}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Serving Size</span>
          <span>{{recipe.servingSize}}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Total Prep Time (minutes)</span>
          <span>{{recipe.prepTimeMin}}</span>
        </div>
        <div class="recipe-overview-item">
          <span>Total Cook Time (minutes)</span>
          <span>{{recipe.cookTimeMin}}</span>
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
    }
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
}
div.recipe-overview {
 padding: 25px;
 padding-top: 0px;

}
div.recipe-overview-item {
  padding: 5px;
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
</style>
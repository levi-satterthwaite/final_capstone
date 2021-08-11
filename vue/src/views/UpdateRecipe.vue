<template>
  <div class="page update-recipe">
    <div class="error" v-if="error">{{ error.message }}</div>
    <h1 v-if="recipe">Update Recipe</h1>
    <p v-if="!recipe">Recipe not found!</p>
    <recipe-form
      v-else
      v-on:submit="handleRecipeFormSubmit"
      v-bind:error="submitRecipeError"
      v-bind:data="recipe"
    />
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import RecipeForm from "@/components/RecipeForm";

export default {
  name: "update-recipe",
  data() {
    return {
      submitRecipeError: null,
      recipe: null,
      error: null
    };
  },
  components: {
    RecipeForm,
  },
  methods: {
    async handleRecipeFormSubmit({ recipe, ingredients, file }) {
      try {
        if (file) {
          const outputFileName = await this.addImage(file);
          recipe.imageFileName = outputFileName;
        }
        recipe.ingredientList = ingredients;
        await this.updateRecipe(recipe);
        this.$router.push({
          name: "recipeDetails",
          params: { id: recipe.recipeId },
        });
      } catch (e) {
        this.submitRecipeError = mealPlannerService.getError(e);
      }
    },
    // Error handling is done in handleRecipeFormSubmit
    async addImage(file) {
      console.log(file);
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async updateRecipe(recipe) {
      const response = await mealPlannerService.updateRecipe(recipe);
      return response.data;
    },
  },
  async created() {
    const recipeId = this.$route.params.id;
    this.recipe = this.$store.state.recipes.find((recipe) => {
      return recipe.recipeId == recipeId;
    });
    if(!this.recipe) {
      try {
        const response = await mealPlannerService.getRecipeById(recipeId);
        this.recipe = response.data;
      } catch(e) {
        this.error = mealPlannerService.getError(e);
      }
    }
  },
};
</script>

<style>
</style>
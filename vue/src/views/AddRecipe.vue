<template>
  <div class="page add-recipe">
    <h1>Create Recipe</h1>
    <recipe-form v-on:submit="handleRecipeFormSubmit" v-bind:error="submitRecipeError" />
  </div>
</template>

<script>

import RecipeForm from "@/components/RecipeForm";
import mealPlannerService from "@/services/MealPlannerService";

export default {
  name: "add-recipe",
  data() {
    return {
      submitRecipeError: null
    }
  },
  components: {
    RecipeForm,
  },
  methods: {
    // {recipe, file, ingredients} is destructing the Object submitData
    // submitData is the value passed into $emit from RecipeForm
    async handleRecipeFormSubmit({ recipe, file, ingredients }) {
      try {
        //const {recipe, file} = submitData
        const outputFileName = await this.addImage(file);
        recipe.imageFileName = outputFileName;
        const savedRecipe = await this.addRecipe(recipe);
        await this.addRecipeIngredients(savedRecipe, ingredients);
        this.$router.push({ name: "recipes" });
      } catch (e) {
        this.submitRecipeError = mealPlannerService.getError(e);
        console.error(e);
      }
    },
    // Error handling is done in handleRecipeFormSubmit
    async addImage(file) {
      console.log(file);
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    // Error handling is done in handleRecipeFormSubmit
    async addRecipe(recipe) {
      console.log(recipe);
      const response = await mealPlannerService.addRecipe(recipe);
      return response.data;
    },
    // Error handling is done in handleRecipeFormSubmit
    async addRecipeIngredients(recipe, ingredients) {
      console.log(recipe, ingredients);
      const response = await mealPlannerService.addIngredientsToRecipe(
        recipe,
        ingredients
      );
      return response.data;
    },
  },
};
</script>

<style>
div.add-recipe {
  display: flex;
  flex-direction: column;
  align-items: center;
}

div.add-recipe h1 {
  color: #4B3F72;
  margin-top: 50px;
}

</style>
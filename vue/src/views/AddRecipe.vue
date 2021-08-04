<template>
  <div class="add-recipe">
    <h1>Create Recipe</h1>
    <recipe-form v-on:submit="handleRecipeFormSubmit" />
  </div>
</template>

<script>
import RecipeForm from "@/components/RecipeForm";
import mealPlannerService from "@/services/MealPlannerService";

export default {
  name: "add-recipe",
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
        console.error(e);
      }
    },
    async addImage(file) {
      console.log(file);
      const response = await mealPlannerService.addImage(file);
      return response.data;
    },
    async addRecipe(recipe) {
      console.log(recipe);
      const response = await mealPlannerService.addRecipe(recipe);
      return response.data;
    },
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

</style>
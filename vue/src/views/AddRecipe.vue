<template>
  <div>
      <h1>Add Recipe</h1>
      <recipe-form v-on:submit="handleRecipeFormSubmit" />
  </div>
</template>

<script>
import RecipeForm from '@/components/RecipeForm';
import mealPlannerService from '@/services/MealPlannerService'

export default {
    name: "add-recipe",
    components: {
        RecipeForm
    },
    methods: {
        // {recipe, file} is destructing the Object submitData 
        // submitData is the value passed into $emit from RecipeForm
        async handleRecipeFormSubmit({recipe, file}) {
            try {
                //const {recipe, file} = submitData
                const outputFileName = await this.addImage(file);
                recipe.imageFileName = outputFileName;
                await this.addRecipe(recipe);
                this.$router.push({name: "recipes"});
            } catch(e) {
                console.error(e);
            }
        },
        async addImage(file) {
            console.log(file);
            const response = await mealPlannerService.addImage(file);
            return response.data;
        },
        async addRecipe(recipe) {
            console.log(recipe)
            return await mealPlannerService.addRecipe(recipe);
        }
    }
}
</script>

<style>

</style>
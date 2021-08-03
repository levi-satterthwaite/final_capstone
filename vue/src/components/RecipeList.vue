<template>
  <div class="recipe-list">
      <recipe-card v-for="recipe in $store.state.recipes" v-bind:key="recipe.id" v-bind:recipe="recipe" />
  </div>
</template>

<script>
import mealPlannerService from '@/services/MealPlannerService';
import RecipeCard from '@/components/RecipeCard';

export default {
    name: 'recipe-list',
    components: {
        RecipeCard
    },
    methods: {
        getAllRecipes() {
            mealPlannerService.getRecipes().then(response => {
                this.$store.commit("SET_RECIPES", response.data)
            }).catch(error => {
                if(error.response) {
                    this.errorMsg = "Error Status " + error.response.status 
                    + ": " + error.response.statusText + ".";
                }
                else if(error.request) {
                    this.errorMsg = "Server did not respond.";
                } else {
                    this.errorMsg = "Something did went wrong!";
                }
            });
        }
    },
    created() {
        this.getAllRecipes();
    }
}
</script>

<style>
div.recipe-list {
    display:flex;
    flex-wrap: wrap;
}
</style>
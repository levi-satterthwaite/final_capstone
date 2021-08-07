<template>
  <div class="page recipes-page">
      <h1>Recipes</h1> 
      <div class="action-bar">
          <router-link to="/recipes/add" tag="button" class="btn">Add Recipe</router-link>
      </div>
      <div class="container">
          <recipe-list />
      </div>
  </div>
</template>

<script>
import RecipeList from '@/components/RecipeList'

export default {
    name: 'recipes',
    components: {
        RecipeList
    }
}
</script>

<style>
</style>
<template>
  <div>
      <h2>Recipes</h2>
      <router-link to="/recipes/add" tag="button">Add Recipe</router-link>
      <recipe-list />
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
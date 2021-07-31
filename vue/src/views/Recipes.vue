<template>
  <div>
      <h2>Recipes</h2>
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
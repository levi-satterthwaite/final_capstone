<template>
  <div class="recipes-page">
      <h2>Recipes</h2>
      <router-link to="/recipes/add" tag="button" class="add-recipe">Add Recipe</router-link>
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
div.recipes-page {
    font-family: Rubik, 'Poppins', sans-serif;
    /* margin-left: auto;
    margin-right: auto; */
    /* display: flex; */
    /* flex-direction: row-reverse;
    order: -1;
    align-content: flex-start; */

    /* display: grid;
    grid-template-columns: 1fr, 1fr, 1fr, 1fr;
    grid-template-areas: ".recipe-card, .recipe-card, .recipe-card, .recipe-card"; */
}

div.container {
    display: flex;
}

</style>
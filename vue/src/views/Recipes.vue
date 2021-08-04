<template>
  <div class="recipes-page">
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
h1 {
    text-align: center;
}
div.container {
    display: flex;
    justify-items: center;

}
div.action-bar {
    margin: 10px;
    margin-right: 30px;
    display: flex;
    justify-content: flex-end;
}

</style>
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
div.recipes h1 {
    text-align: center;
    margin-top: 50px;
}

div.recipes-page {
    display: flex;
    flex-direction: column;
}

div.recipes-page div.container {
    display: flex;
    justify-items: center;
    margin-left: 30px;
    margin-right: 30px;

}
div.action-bar {
    display: flex;
    justify-content: flex-end;
}

</style>
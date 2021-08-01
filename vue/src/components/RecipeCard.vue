<template>
  <div id="recipeCard">
      <!-- <img src="" alt="recipe.name" > -->
      <h2>{{recipe.name}}</h2>
      <p>{{recipe.category}}</p>
      <p>{{recipe.difficultyLevel}}</p>
  </div>
</template>

<script>
export default {
    name: "recipe-card",
    props: ['recipe'],
    methods: {

    }

}
</script>

<style>

</style>
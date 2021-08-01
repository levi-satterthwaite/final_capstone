<template>
  <div class="recipe-card">
      <img v-bind:src="'/files/'+ recipe.imageFileName" v-bind:alt="recipe.name" >
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
div.recipe-card img {
    width: 200px;
}
</style>
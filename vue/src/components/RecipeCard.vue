<template>
  <div class="recipe-card card">
      <div class="shadow"></div>
      <div class="image-wrapper">
          <img v-bind:src="'/files/'+ recipe.imageFileName" v-bind:alt="recipe.name" >
      </div>
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
div.recipe-card {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    width: 20em;
    max-width: 20em;
    margin: 10px;
    padding: 15px;

}
.shadow {
    margin: 20px;
    background-color: rgb(68, 68, 68);
    box-shadow: 5px 10px;
}
div.image-wrapper {
    max-height: 15em;

}
div.recipe-card img {
    width: 100%;
    height: 100%;
    min-height: 15em;

}
</style>
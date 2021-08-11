<template>
  <div class="recipe-card card">
    <router-link
      v-bind:to="{ name: 'recipeDetails', params: { id: recipe.recipeId } }"
    >
      <div class="image-wrapper">
        <img
          v-bind:src="'/files/' + recipe.imageFileName"
          v-bind:alt="recipe.name"
        />
      </div>
      <div class="content-wrapper">
        <h2>{{ recipe.name }}</h2>
        <div class="category">
          <p>{{ recipe.category }} | {{ recipe.difficultyLevel }}</p>
        </div>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "recipe-card",
  props: ["recipe"],
  methods: {},
};
</script>

<style>
div.recipe-card {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  width: 20em;
  max-width: 20em;
  margin: 20px;
}
div.content-wrapper {
  margin: 20px;
}
h2 {
  font-size: 27px;
  margin: 0px;
  white-space: -moz-pre-wrap !important; /* Mozilla, since 1999 */
  white-space: -webkit-pre-wrap; /* Chrome & Safari */
  white-space: -pre-wrap; /* Opera 4-6 */
  white-space: -o-pre-wrap; /* Opera 7 */
  white-space: pre-wrap; /* CSS3 */
  word-wrap: break-word; /* Internet Explorer 5.5+ */
  word-break: break-all;
  white-space: normal;
}
div.category {
  font-size: 20px;
}
div.image-wrapper {
  max-height: 15em;
}
div.recipe-card img {
  /* width: 100%;
  height: 100%;
  min-height: 15em;
  max-height: 5px;
  display: flex;
  border-top-right-radius: 10px;
  border-top-left-radius: 10px; */
  min-height: 15em;
  max-height: 5px;
  display: flex;
  border-top-right-radius: 10px;
  border-top-left-radius: 10px;
  float: center;
  width: 100%;
  height: 100%;
  overflow: hidden;
  object-fit: cover;
}
a {
  display: block;
  text-decoration: none;
}
</style>
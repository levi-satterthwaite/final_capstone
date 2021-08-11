<template>
  <div class="meal-card card">
    <router-link
      v-bind:to="{ name: 'mealDetails', params: { id: meal.mealId } }"
    >
      <div class="image-wrapper">
        <img
          v-bind:src="'/files/' + meal.imageFileName"
          v-bind:alt="meal.name"
        />
      </div>
      <div class="content-wrapper">
        <h2>{{ meal.name }}</h2>
        <div class="category">
          <p>{{ meal.category }}</p>
        </div>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "meal-card",
  props: ["meal"],
  methods: {},
};
</script>

<style>
div.meal-card {
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
div.meal-card img {
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
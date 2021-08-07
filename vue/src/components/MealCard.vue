<template>
  <div class="meal-card card">
    <router-link v-bind:to="{name: 'mealDetails', params: {id: meal.mealId}}">
      <div class="image-wrapper">
        <img
          v-bind:src="'/files/' + meal.imageFileName"
          v-bind:alt="meal.name"
        />
      </div>
      <div class="content-wrapper">
        <h2>{{ meal.name }}</h2>
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
  margin: 30px;
}
div.content-wrapper {
  margin: 20px;
}
h2 {
  font-size: 27px;
  margin: 0px;
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
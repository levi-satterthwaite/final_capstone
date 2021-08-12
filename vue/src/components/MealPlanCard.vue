<template>
  <div class="meal-plan-card card">
    <router-link
      v-bind:to="{
        name: 'mealPlanDetails',
        params: { id: mealPlan.mealPlanId },
      }"
    >
      <div class="image-wrapper">
        <img
          v-bind:src="'/files/' + mealPlan.imageFileName"
          v-bind:alt="mealPlan.name"
        />
      </div>
      <div class="content-wrapper">
        <h2>{{ mealPlan.name }}</h2>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "meal-plan-card",
  props: ["mealPlan"],
  methods: {},
};
</script>

<style>
div.meal-plan-card {
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

div.category {
  font-size: 20px;
}
div.image-wrapper {
  max-height: 15em;
}
div.meal-plan-card img {
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
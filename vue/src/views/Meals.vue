<template>
  <div class="meal-page">
    <h1>Meals</h1>
    <div class="action-bar">
      <router-link to="/meals/add" tag="button" class="btn">
        Add Meal</router-link
      >
    </div>
    <div class="container">
      <meal-list />
    </div>
  </div>
</template>

<script>
import MealList from "@/components/MealList";

export default {
  name: "meals",
  components: {
    MealList,
  },
};
</script>

<style>
div.meal-page {
  display: flex;
  flex-direction: column;
  margin-left: 50px;
  margin-right: 50px;
}
div.meal-page h1 {
  text-align: left;
  margin-top: 50px;
  color: #4b3f72
}
div.meal-page div.container {
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
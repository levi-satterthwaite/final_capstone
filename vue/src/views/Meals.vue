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
h1 {
  text-align: center;
  margin: 50px;
}

div.meal-page {
  display: flex;
  flex-direction: column;
}

div.meal-page div.container {
  display: flex;
  justify-items: center;
  margin-left: 30px;
  margin-right: 30px;
}
div.action-bar {
  margin: 10px;
  margin-right: 30px;
  display: flex;
  justify-content: flex-end;
}
</style>
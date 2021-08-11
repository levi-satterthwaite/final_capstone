<template>
  <div class="card meal-plan-grocery-list">
    <div class="error" v-if="error">{{ error.message }}</div>
    <div
      class="ingredient-category"
      v-for="category in ingredientsByCategory.keys()"
      v-bind:key="category"
    >
      <h3>{{ category }}</h3>
      <div class="ingredient-list">
        <ul class="groceries">
          <li
            v-for="ingredient in ingredientsByCategory.get(category)"
            v-bind:key="ingredient.ingredientId"
            v-bind:ingredient="ingredient"
          >
            <div class="ingredient-name">
              {{ ingredient.name }}
            </div>
            <div class="ingredient-quantities">
              <div
                class="ingredient-quantity"
                v-for="quantity in ingredient.quantities"
                v-bind:key="
                  ingredient.name + quantity.quantity + quantity.unitMeasurement
                "
              >
                <span class="ingredient-amount">{{ quantity.quantity }}</span
                >&nbsp;
                <span class="ingredient-unit-measurement">{{
                  quantity.unitMeasurement
                }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "meal-plan-groceries",
  props: {
    ingredientsByCategory: Map,
    error: Error,
  },
};
</script>

<style>
div.meal-plan-grocery-list {
  width: 50%;
  background-color: white;
  padding: 25px;
}
div.ingredient-category h3 {
  font-size: 20px;
  /* text-align: center; */
  /* background-color: #9d7dde; */
  color: #9d7dde;
  padding-left: 30px;
}
div.ingredient-category ul.groceries {
  font-size: 17px;
  color: #4b3f72;
  margin-left: 10px;
}
div.ingredient-category ul.groceries li {
  padding: 10px;
  list-style-type: none;
}
div.ingredient-category ul.groceries li div.ingredient-name {
  font-weight: bold;
}
div.ingredient-category ul.groceries li div.ingredient-quantities {
  margin-left: 30px;
}
</style>
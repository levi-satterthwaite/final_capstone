<template>
  <div class="meal-plan-form card">
    <form v-on:submit.prevent="submitMealPlan">
      <fieldset class="meal-plan-details">
        <legend>Meal Plan Details</legend>
        <div class="field">
          <label for="name">Name</label>
          <input type="text" id="name" v-model="mealPlan.name" required />
        </div>
        <div class="field">
          <label for="description">Description</label>
          <input type="text" id="description" v-model="mealPlan.description" />
        </div>
      </fieldset>
      <fieldset class="meal-plan-meals">
        <legend>Meal Plan Meals</legend>
        <ul class="meals">
          <li
            class="meal meal-empty"
            v-if="!hasMeals && !isAddMealOpen"
          >
            Please Add Meals
          </li>
          <li
            class="meal"
            v-else
            v-for="meal in meals"
            v-bind:key="meal.mealId"
            v-bind:meals="meals"
          >
            <div class="field">
              <span class="meal-name">{{ meal.name }}</span>
            </div>
            <div class="form-controls align-right">
              <button
                class="btn btn-sm"
                v-on:click.prevent="removeMeal(meal)"
              >
                Remove
              </button>
            </div>
          </li>
        </ul>
        <div class="form-controls align-right">
          <button
            class="btn btn-sm"
            v-show="!isAddMealOpen"
            v-on:click.prevent="openMealSearch"
          >
            Add Meal
          </button>
        </div>
        <div class="field" v-if="isAddMealOpen">
          <label for="searchMeal">Search Meals</label>
          <search-autocomplete
            id="searchMeal"
            v-bind:search-value="searchTerm"
            v-bind:get-data="getMeals"
            v-on:result="addMeal"
            v-on:change="setSearchTerm"
            v-bind:use-add="false"
          />
          <div class="form-controls align-right">
            <button class="btn btn-sm">Cancel</button>
          </div>
        </div>
      </fieldset>
      <fieldset class="meal-plan-image">
        <legend>Meal Plan Image</legend>
        <div class="field">
          <label for="image">Image</label>
          <input type="file" id="image" v-on:change="onFileChange" required />
        </div>
        <div class="form-controls">
          <button v-on:click.prevent="chooseImage" class="btn btn-sm">
            Choose File
          </button>
        </div>
        <div class="image-preview" v-if="image">
          <img v-bind:src="image" />
        </div>
      </fieldset>
      <div class="error" v-if="error">{{ error.message }}</div>
      <div class="form-controls align-right">
        <button class="btn" type="submit">Submit</button>
      </div>
    </form>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import SearchAutocomplete from "@/components/SearchAutocomplete";

export default {
  name: "meal-plan-form",
  props: {
    error: {
      type: Object,
      required: false,
      default: null,
    },
  },
  data() {
    return {
      mealPlan: {},
      image: "",
      file: "",
      meals: [],
      isAddMealOpen: false,
      searchTerm: "",
      errors: {},
      categories: [],
      categoriesError: null,
    };
  },
  components: {
    SearchAutocomplete,
  },
  computed: {
    hasMeals() {
      return this.meals.length > 0;
    },
    hasErrors() {
      return Object.keys(this.errors).length > 0;
    },
  },
  methods: {
    validateForm() {
      const errors = {};
      this.errors = errors;
    },
    submitMealPlan() {
      this.validateForm();
      if (this.hasErrors) {
        return;
      }
      const submitData = {
        mealPlan: this.mealPlan,
        file: this.file,
        meals: this.meals.map((meal) => {
          return {
            ...meal,
          };
        }),
      };
      this.$emit("submit", submitData);
    },
    onFileChange(event) {
      const files = event.target.files;
      if (!files.length) {
        return;
      }
      this.file = files[0];
      this.createImage(this.file);
    },
    createImage(file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        this.image = event.target.result;
      };
      reader.readAsDataURL(file);
    },
    async getMeals() {
      const response = await mealPlannerService.getMeals();
      return response.data;
    },
    addMeal(meal) {
      this.meals = [...this.meals, meal];
      this.closeMealSearch();
    },
    openMealSearch() {
      this.isAddMealOpen = true;
    },
    closeMealSearch() {
      this.isAddMealOpen = false;
    },
    setSearchTerm(searchTerm) {
      this.searchTerm = searchTerm;
    },
    removeMeal(meal) {
      this.meals = this.meals.filter((candidate) => {
        return candidate.mealId !== meal.mealId;
      });
    },
    chooseImage() {
      document.getElementById("image").click();
    },
  },
  async mounted() {
    try {
      const response = await mealPlannerService.getMealCategories();
      this.categories = response.data;
    } catch (e) {
      this.categoriesError = mealPlannerService.getError(e);
    }
  }
};
</script>

<style>
div.field {
  margin: 10px;
  margin-bottom: 20px;
}
div.field label {
  /* display: none; */
  display: block;
  /* width: 175px;
  display: inline-block; */
  margin-bottom: 5px;
}
div.field input {
  width: 96%;
  font-size: 16px;
  line-height: 30px;
  border: none;
  border-color: black;
  border-style: solid;
  border-radius: 10px;
  border-width: 1.5px;
  padding: 5px;
  padding-left: 10px;
  padding-right: 10px;
}
div.field input:focus {
  outline: none;
  border-color: #9d7dde;
}
div.image-preview img {
  max-width: 200px;
}
input#image {
  display: none;
}
div.meal-plan-form {
  padding: 30px;
  width: 50%;
  background-color: white;
}
div.meal-plan-form fieldset {
  margin: 10px;
  margin-bottom: 20px;
}
ul.meals {
  list-style: none;
  padding: 0px;
  margin: 10px;
}

div.meal-plan-form fieldset {
  /* border-radius: 10px; */
  border: none;
  border-top: solid 1.5px #9d7dde;
  position: relative;
  margin-top: 50px;
  padding-bottom: 30px;
}
div.meal-plan-form fieldset legend {
  color: #4b3f72;
  font-weight: bold;
  position: absolute;
  top: -35px;
  left: 0px;
}
ul.meals {
  margin: 0px;
}
li.meal {
  border-bottom: solid 1px #ccc;
  padding-top: 10px;
  padding-bottom: 10px;
}
div.error {
  color: red;
}
</style>
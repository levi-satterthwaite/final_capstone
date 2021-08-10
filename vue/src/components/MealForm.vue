<template>
  <div class="form meal-form card">
    <form v-on:submit.prevent="submitMeal">
      <fieldset class="meal-details">
        <legend>Meal Details</legend>
        <div class="field">
          <label for="name">Name</label>
          <input type="text" id="name" v-model="meal.name" required />
        </div>
        <div class="field">
          <label for="category">Category</label>
          <select
            name="category"
            id="category"
            v-model="meal.category"
            required
          >
            <option value=""></option>
            <option
              v-for="category in categories"
              v-bind:key="category.name"
              v-bind:value="category.name"
            >
              {{ category.name }}
            </option>
          </select>
        </div>
      </fieldset>
      <fieldset class="meal-recipes">
        <legend>Meal Recipes</legend>
        <ul class="items recipes">
          <li
            class="recipe recipe-empty"
            v-if="!hasRecipes && !isAddRecipeOpen"
          >
            Please Add Recipes
          </li>
          <li
            class="recipe"
            v-else
            v-for="recipe in recipes"
            v-bind:key="recipe.recipeId"
          >
            <div class="field">
              <span class="recipe-name">{{ recipe.name }}</span>
            </div>
            <div class="form-controls align-right">
              <button
                class="btn btn-sm"
                v-on:click.prevent="removeRecipe(recipe)"
              >
                Remove
              </button>
            </div>
          </li>
        </ul>
        <div class="form-controls align-right">
          <button
            class="btn btn-sm"
            v-show="!isAddRecipeOpen"
            v-on:click.prevent="openRecipeSearch"
          >
            Add Recipe
          </button>
        </div>
        <div class="field" v-if="isAddRecipeOpen">
          <label for="searchRecipe">Search Recipes</label>
          <search-autocomplete
            id="searchRecipe"
            v-bind:search-value="searchTerm"
            v-bind:get-data="getRecipes"
            v-on:result="addRecipe"
            v-on:change="setSearchTerm"
            v-bind:use-add="false"
          />
          <div class="form-controls align-right">
            <button class="btn btn-sm">Cancel</button>
          </div>
        </div>
      </fieldset>
      <fieldset class="meal-image">
        <legend>Meal Image</legend>
        <div class="field">
          <label for="image">Image</label>
          <input type="file" id="image" v-on:change="onFileChange" />
        </div>
        <div class="form-controls">
          <button v-on:click.prevent="chooseImage" class="btn btn-sm">
            Choose File
          </button>
        </div>
        <div class="image-preview" v-if="image">
          <img v-bind:src="image" />
        </div>
        <div class="image-preview" v-if="data && data.imageFileName && !image">
          <img
            v-bind:src="'/files/' + data.imageFileName"
            v-bind:alt="meal.name"
          />
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
  name: "meal-form",
  props: {
    error: {
      type: Object,
      required: false,
      default: null,
    },
    data: {
      type: Object,
      required: false,
      default: null,
    },
  },
  data() {
    return {
      meal: {},
      image: "",
      file: "",
      recipes: [],
      isAddRecipeOpen: false,
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
    hasRecipes() {
      return this.recipes.length > 0;
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
    submitMeal() {
      this.validateForm();
      if (this.hasErrors) {
        return;
      }
      const submitData = {
        meal: this.meal,
        file: this.file,
        recipes: this.recipes.map((recipe) => {
          return {
            ...recipe,
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
    async getRecipes() {
      const response = await mealPlannerService.getRecipes();
      return response.data;
    },
    addRecipe(recipe) {
      if (!recipe) {
        return;
      }
      this.recipes = [...this.recipes, recipe];
      this.closeRecipeSearch();
    },
    openRecipeSearch() {
      this.isAddRecipeOpen = true;
    },
    closeRecipeSearch() {
      this.isAddRecipeOpen = false;
    },
    setSearchTerm(searchTerm) {
      this.searchTerm = searchTerm;
    },
    removeRecipe(recipe) {
      this.recipes = this.recipes.filter((candidate) => {
        return candidate.recipeId !== recipe.recipeId;
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
  },
  created() {
    if (this.data) {
      this.meal = this.data;
      this.recipes = this.data.recipeList;
    }
  },
};
</script>

<style>



/* ul.recipes {
  margin: 0px;
} */

</style>
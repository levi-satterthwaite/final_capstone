<template>
  <div class="meal-form card">
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
        <ul class="recipes">
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
            v-bind:recipes="recipes"
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
div.meal-form {
  padding: 30px;
  width: 50%;
  background-color: white;
}
div.meal-form fieldset {
  margin: 10px;
  margin-bottom: 20px;
}
ul.recipes {
  list-style: none;
  padding: 0px;
  margin: 10px;
}

div.meal-form fieldset {
  /* border-radius: 10px; */
  border: none;
  border-top: solid 1.5px #9d7dde;
  position: relative;
  margin-top: 50px;
  padding-bottom: 30px;
}
div.meal-form fieldset legend {
  color: #4b3f72;
  font-weight: bold;
  position: absolute;
  top: -35px;
  left: 0px;
}
ul.recipes {
  margin: 0px;
}
li.recipe {
  border-bottom: solid 1px #ccc;
  padding-top: 10px;
  padding-bottom: 10px;
}
div.error {
  color: red;
}
</style>
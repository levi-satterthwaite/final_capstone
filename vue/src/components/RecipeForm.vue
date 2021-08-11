<template>
  <div class="form recipe-form card">
    <form v-on:submit.prevent="submitRecipe">
      <fieldset class="recipe-details">
        <legend>Recipe Details</legend>
        <div class="field">
          <label for="name">Name</label>
          <input type="text" id="name" v-model="recipe.name" required />
          <!-- title="Name" placeholder="Name" aria-placeholder="Name" -->
        </div>
        <div class="field">
          <label for="category">Category</label>
          <select
            name="category"
            id="category"
            v-model="recipe.category"
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
          <!-- <input type="text" id="category" v-model="recipe.category" required /> -->
        </div>
        <div class="field">
          <label for="difficultyLevel">Difficulty Level</label>
          <select
            name="difficultyLevel"
            id="difficultyLevel"
            v-model="recipe.difficultyLevel"
            required
          >
            <option value=""></option>
            <option
              v-for="difficultyLevel in difficultyLevels"
              v-bind:key="difficultyLevel.name"
              v-bind:value="difficultyLevel.name"
            >
              {{ difficultyLevel.name }}
            </option>
          </select>
          <!-- <input
            type="text"
            id="difficultyLevel"
            v-model="recipe.difficultyLevel"
            required
          /> -->
        </div>
        <div class="field">
          <label for="prepTimeMin">Total Prep Time</label>
          <input
            type="number"
            id="prepTimeMin"
            v-model="recipe.prepTimeMin"
            min="0"
            required
          />
          <div class="error" v-if="errors.prepTimeMin">
            {{ errors.prepTimeMin }}
          </div>
        </div>
        <div class="field">
          <label for="cookTimeMin">Total Cook Time</label>
          <input
            type="number"
            id="cookTimeMin"
            v-model="recipe.cookTimeMin"
            min="0"
            required
          />
          <div class="error" v-if="errors.cookTimeMin">
            {{ errors.cookTimeMin }}
          </div>
        </div>
        <div class="field">
          <label for="servingSize">Serving Size</label>
          <input
            type="number"
            id="servingSize"
            v-model="recipe.servingSize"
            required
          />
          <div class="error" v-if="errors.servingSize">
            {{ errors.servingSize }}
          </div>
        </div>
        <div class="field">
          <label for="instructions">Instructions</label>
          <textarea
            type="text"
            id="instructions"
            v-model="recipe.instructions"
            required
          ></textarea>
        </div>
      </fieldset>
      <fieldset class="recipe-ingredients">
        <legend>Recipe Ingredients</legend>
        <ul class="items ingredients">
          <li
            class="ingredient ingredient-empty"
            v-if="!hasIngredients && !isAddIngredientOpen"
          >
            Please Add Ingredients
          </li>
          <li
            class="ingredient"
            v-else
            v-for="ingredient in ingredients"
            v-bind:key="ingredient.ingredientId"
          >
            <div class="field">
              <span class="ingredient-name">{{ ingredient.name }}</span>
            </div>
            <div class="field">
              <span class="ingredient-category">{{ ingredient.category }}</span>
            </div>
            <div class="field">
              <label v-bind:for="ingredient.ingredientId + '-quantity'"
                >Quantity</label
              >
              <input
                type="number"
                step="0.01"
                v-bind:id="ingredient.ingredientId + '-quantity'"
                v-model="ingredient.quantity"
                required
              />
              <div
                class="error"
                v-if="
                  errors[ingredient.ingredientId] &&
                  errors[ingredient.ingredientId].quantity
                "
              >
                {{ errors[ingredient.ingredientId].quantity }}
              </div>
            </div>
            <div class="field">
              <label v-bind:for="ingredient.ingredientId + '-unit-measurement'">
                Unit of Measurement</label
              >
              <input
                type="text"
                v-bind:id="ingredient.ingredientId + '-unit-measurement'"
                v-model="ingredient.unitMeasurement"
                required
              />
            </div>
            <div class="form-controls align-right">
              <button
                class="btn btn-sm"
                v-on:click.prevent="removeIngredient(ingredient)"
              >
                Remove
              </button>
            </div>
          </li>
        </ul>
        <div class="form-controls align-right">
          <button
            class="btn btn-sm"
            v-show="!isAddIngredientOpen"
            v-on:click.prevent="openIngredientSearch"
          >
            Add Ingredient
          </button>
        </div>
        <div
          class="field"
          v-if="isAddIngredientOpen && !isAddNewIngredientOpen"
        >
          <label for="searchIngredient">Search Ingredients</label>
          <search-autocomplete
            id="searchIngredient"
            v-bind:search-value="searchTerm"
            v-bind:get-data="getIngredients"
            v-on:result="addIngredient"
            v-on:change="setSearchTerm"
            v-on:add="openAddNewIngredient"
          />
          <div class="form-controls align-right">
            <button
              class="btn btn-sm"
              v-on:click.prevent="closeIngredientSearch"
            >
              Cancel
            </button>
          </div>
        </div>
        <div class="add-new-ingredient" v-if="isAddNewIngredientOpen">
          <div>Add New Ingredient</div>
          <div class="field">
            <label for="newIngredientName">Ingredient Name</label>
            <input
              type="text"
              id="newIngredientName"
              v-model="newIngredient.name"
            />
          </div>
          <div class="field">
            <label for="newIngredientCategory">Ingredient Category</label>
            <input
              type="text"
              id="newIngredientCategory"
              v-model="newIngredient.category"
              required
            />
          </div>
          <div class="error" v-if="saveNewIngredientError">
            {{ saveNewIngredientError.message }}
          </div>
          <div class="form-controls">
            <button class="btn btn-sm" v-on:click.prevent="saveNewIngredient">
              Save
            </button>
            <button
              class="btn btn-sm"
              v-on:click.prevent="closeAddNewIngredient"
            >
              Cancel
            </button>
          </div>
        </div>
      </fieldset>
      <fieldset class="recipe-image">
        <legend>Recipe Image</legend>
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
            v-bind:alt="recipe.name"
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
import SearchAutocomplete from "@/components/SearchAutocomplete";
import mealPlannerService from "@/services/MealPlannerService";

export default {
  name: "recipe-form",
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
      saveNewIngredientError: null,
      recipe: {},
      image: "",
      file: "",
      ingredients: [],
      isAddIngredientOpen: false,
      isAddNewIngredientOpen: false,
      searchTerm: "",
      newIngredient: {},
      errors: {},
      categories: [],
      categoriesError: null,
      difficultyLevels: [],
      difficultyLevelsError: null,
    };
  },
  components: {
    SearchAutocomplete,
  },
  computed: {
    hasIngredients() {
      return this.ingredients.length > 0;
    },
    hasErrors() {
      return Object.keys(this.errors).length > 0;
    },
  },
  methods: {
    validateForm() {
      const errors = {};
      if (this.recipe.prepTimeMin < 0) {
        errors.prepTimeMin = "Cannot be a negative value.";
      }
      if (this.recipe.cookTimeMin < 0) {
        errors.cookTimeMin = "Cannot be a negative value.";
      }
      if (this.recipe.servingSize < 0) {
        errors.servingSize = "Cannot be a negative value.";
      }
      for (const ingredient of this.ingredients) {
        if (ingredient.quantity < 0) {
          errors[ingredient.ingredientId] = {};
          errors[ingredient.ingredientId].quantity =
            "Cannot be a negative value.";
        }
      }

      this.errors = errors;
    },
    submitRecipe() {
      // $emit() triggers an event called "submit" and passes in recipe
      // and file (image) as its arguments
      this.validateForm();
      if (this.hasErrors) {
        return;
      }
      const submitData = {
        recipe: this.recipe,
        file: this.file,
        // we need to parse the quantity from a string to a number so using
        // map to transform each ingredient's quantity from a string to a number
        ingredients: this.ingredients.map((ingredient) => {
          return {
            ...ingredient,
            quantity: parseFloat(ingredient.quantity),
          };
        }),
      };
      this.$emit("submit", submitData);
    },
    // whenever we change the file input value, this will trigger a change event
    // and call onFileChange
    onFileChange(event) {
      // .target is the input element (in this case the choose file button)
      // that the event was fired on
      // whatever file the user selects, it will trigger the event on the input element
      // with a property called files, which is a list of the files the user selected
      const files = event.target.files;
      if (!files.length) {
        // returns nothing if there is nothing in the files array, which means
        // the user did not select any files
        return;
      }
      this.file = files[0];
      this.createImage(this.file);
    },
    createImage(file) {
      const reader = new FileReader();
      // .onload is a function of the FileReader instance
      // once the reader has finished loading, store the image data into
      // the property image
      reader.onload = (event) => {
        // this event is being targeted by the reader
        this.image = event.target.result;
      };
      reader.readAsDataURL(file);
    },
    // async/await handles the .then(), the .catch(), and the .finally()
    // instead we must use try/catches to handle error
    async getIngredients() {
      // await returns the value resolved by the promise (similar to .then())
      const response = await mealPlannerService.getIngredients();
      // this line of code does not occur until the above promise is resolved or rejected
      // need to wrap all awaits around try / catches 1
      return response.data;
    },
    addIngredient(ingredient) {
      // we want to create a new Array with the existing ingredients and the ingredient to add
      this.ingredients = [...this.ingredients, ingredient];
      // after the ingredient is added, we close the add ingredient form
      this.closeIngredientSearch();
    },
    openIngredientSearch() {
      this.isAddIngredientOpen = true;
    },
    closeIngredientSearch() {
      this.isAddIngredientOpen = false;
    },
    openAddNewIngredient() {
      this.isAddNewIngredientOpen = true;
      // if the user searches for an ingredient that does not exist, they can clicks
      // the add new ingredient button to add the ingredient and populates the new
      // ingredient's name with the search term
      this.newIngredient.name = this.searchTerm;
    },
    closeAddNewIngredient() {
      this.newIngredient = {};
      this.isAddNewIngredientOpen = false;
    },
    async saveNewIngredient() {
      try {
        // first addIngredient() is the promise that adds the ingredient to the
        // database
        const response = await mealPlannerService.addIngredient(
          this.newIngredient
        );
        // the response contains the ingredient that was added to the database
        const ingredient = response.data;
        // this addIngredient() stores the ingredient into the form
        this.addIngredient(ingredient);
        this.closeAddNewIngredient();
      } catch (e) {
        this.saveNewIngredientError = mealPlannerService.getError(e);
      }
    },
    setSearchTerm(searchTerm) {
      // setting the search term so when the user clicks add new ingredient, the
      // input for ingredient.name will be populated with their search term
      this.searchTerm = searchTerm;
    },
    removeIngredient(ingredient) {
      // the candidate represents every ingredient in the list
      // this filter will filter out the candidate that matches the passed
      // in ingredient
      this.ingredients = this.ingredients.filter((candidate) => {
        return candidate.ingredientId !== ingredient.ingredientId;
      });
    },
    chooseImage() {
      document.getElementById("image").click();
    },
  },
  async mounted() {
    try {
      const response = await mealPlannerService.getRecipeCategories();
      this.categories = response.data;
    } catch (e) {
      console.log(e);
      this.categoriesError = mealPlannerService.getError(e);
    }

    try {
      const response = await mealPlannerService.getRecipeDifficultyLevels();
      this.difficultyLevels = response.data;
    } catch (e) {
      console.log(e);
      this.difficultyLevelsError = mealPlannerService.getError(e);
    }
  },
  created() {
    if (this.data) {
      this.recipe = this.data;
      this.ingredients = this.data.ingredientList;
    }
  },
};
</script>

<style>
</style>
<template>
  <div>
    <form v-on:submit.prevent="submitRecipe">
      <div>
        <label for="name">Name:</label>
        <input type="text" id="name" v-model="recipe.name" required />
      </div>
      <div>
        <label for="category">Category:</label>
        <input type="text" id="category" v-model="recipe.category" required />
      </div>
      <div>
        <label for="difficultyLevel">Difficulty Level:</label>
        <input
          type="text"
          id="difficultyLevel"
          v-model="recipe.difficultyLevel"
          required
        />
      </div>
      <div>
        <label for="prepTimeMin">Total Prep Time:</label>
        <input
          type="number"
          id="prepTimeMin"
          v-model="recipe.prepTimeMin"
          required
        />
      </div>
      <div>
        <label for="cookTimeMin">Total Cook Time:</label>
        <input
          type="number"
          id="cookTimeMin"
          v-model="recipe.cookTimeMin"
          required
        />
      </div>
      <div>
        <label for="servingSize">Serving Size:</label>
        <input
          type="number"
          id="servingSize"
          v-model="recipe.servingSize"
          required
        />
      </div>
      <div>
        <label for="instructions">Instructions:</label>
        <input
          type="text"
          id="instructions"
          v-model="recipe.instructions"
          required
        />
      </div>
      <div>
        <div>Recipe Ingredients</div>
        <ul>
          <li v-if="!hasIngredients">Add some ingredients!</li>
          <li
            v-else
            v-for="ingredient in ingredients"
            v-bind:key="ingredient.id"
            v-bind:ingredients="ingredients"
          >
            <div>{{ ingredient.name }}</div>
            <div>{{ ingredient.category }}</div>
            <div>
              <label v-bind:for="ingredient.id + '-quantity'">Quantity:</label>
              <input
                type="number"
                v-bind:id="ingredient.id + '-quantity'"
                v-model="ingredient.quantity"
              />
            </div>
            <div>
              <label v-bind:for="ingredient.id + '-unit-measurement'">
                Unit of Measurement:</label
              >
              <input
                type="text"
                v-bind:id="ingredient.id + '-unit-measurement'"
                v-model="ingredient.unitMeasurement"
              />
            </div>
            <div>
              <button v-on:click.prevent="removeIngredient(ingredient)">
                Remove
              </button>
            </div>
          </li>
        </ul>
        <div>
          <button
            v-show="!isAddIngredientOpen"
            v-on:click.prevent="openIngredientSearch"
          >
            Add Ingredient
          </button>
        </div>
      </div>
      <div v-if="isAddIngredientOpen && !isAddNewIngredientOpen">
        <label for="searchIngredient">Search Ingredients:</label>
        <search-autocomplete
          id="searchIngredient"
          v-bind:search-value="searchTerm"
          v-bind:get-data="getIngredients"
          v-on:result="addIngredient"
          v-on:change="setSearchTerm"
        />
        <button v-on:click.prevent="closeIngredientSearch">Cancel</button>
        <button v-on:click.prevent="openAddNewIngredient">
          Add New Igredient
        </button>
      </div>
      <div v-show="isAddNewIngredientOpen">
        <div>Add New Ingredient</div>
        <div>
          <label for="newIngredientName">Ingredient Name:</label>
          <input
            type="text"
            id="newIngredientName"
            v-model="newIngredient.name"
          />
        </div>
        <div>
          <label for="newIngredientName">Ingredient Category:</label>
          <input
            type="text"
            id="newIngredientCategory"
            v-model="newIngredient.category"
          />
        </div>
        <div>
          <button v-on:click.prevent="saveNewIngredient">Save</button>
          <button v-on:click.prevent="closeAddNewIngredient">Cancel</button>
        </div>
      </div>
      <div>
        <label for="image">Image:</label>
        <input type="file" id="image" v-on:change="onFileChange" required />
      </div>
      <div class="image-preview" v-if="image">
        <img v-bind:src="image" />
      </div>
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
import SearchAutocomplete from "@/components/SearchAutocomplete";
import mealPlannerService from "@/services/MealPlannerService";

export default {
  name: "recipe-form",
  data() {
    return {
      error: null,
      recipe: {},
      image: "",
      file: "",
      ingredients: [],
      isAddIngredientOpen: false,
      isAddNewIngredientOpen: false,
      searchTerm: "",
      newIngredient: {},
    };
  },
  components: {
    SearchAutocomplete,
  },
  methods: {
    submitRecipe() {
      // $emit() triggers an event called "submit" and passes in recipe
      // and file (image) as its arguments
      const submitData = {
        recipe: this.recipe,
        file: this.file,
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
      } catch (error) {
        console.error(error);
        this.error = error;
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
  },
  computed: {
    hasIngredients() {
      return this.ingredients.length > 0;
    },
  },
};
</script>

<style>
div.image-preview img {
  max-width: 200px;
}
</style>
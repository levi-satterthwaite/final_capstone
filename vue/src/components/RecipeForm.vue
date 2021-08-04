<template>
  <div class="recipe-form card">
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
          <input type="text" id="category" v-model="recipe.category" required />
        </div>
        <div class="field">
          <label for="difficultyLevel">Difficulty Level</label>
          <input
            type="text"
            id="difficultyLevel"
            v-model="recipe.difficultyLevel"
            required
          />
        </div>
        <div class="field">
          <label for="prepTimeMin">Total Prep Time</label>
          <input
            type="number"
            id="prepTimeMin"
            v-model="recipe.prepTimeMin"
            required
          />
        </div>
        <div class="field">
          <label for="cookTimeMin">Total Cook Time</label>
          <input
            type="number"
            id="cookTimeMin"
            v-model="recipe.cookTimeMin"
            required
          />
        </div>
        <div class="field">
          <label for="servingSize">Serving Size</label>
          <input
            type="number"
            id="servingSize"
            v-model="recipe.servingSize"
            required
          />
        </div>
        <div class="field">
          <label for="instructions">Instructions</label>
          <input
            type="text"
            id="instructions"
            v-model="recipe.instructions"
            required
          />
        </div>
      </fieldset>
      <fieldset class="recipe-ingredients">
        <legend>Recipe Ingredients</legend>
        <ul class="ingredients">
          <li class="ingredient ingredient-empty" v-if="!hasIngredients">
            Add some ingredients!
          </li>
          <li
            class="ingredient"
            v-else
            v-for="ingredient in ingredients"
            v-bind:key="ingredient.id"
            v-bind:ingredients="ingredients"
          >
            <div class="field">
              <span class="ingredient-name">{{ ingredient.name }}</span>
            </div>
            <div class="field">
              <span class="ingredient-category">{{ ingredient.category }}</span>
            </div>
            <div class="field">
              <label v-bind:for="ingredient.id + '-quantity'">Quantity</label>
              <input
                type="number"
                v-bind:id="ingredient.id + '-quantity'"
                v-model="ingredient.quantity"
                required
              />
            </div>
            <div class="field">
              <label v-bind:for="ingredient.id + '-unit-measurement'">
                Unit of Measurement</label
              >
              <input
                type="text"
                v-bind:id="ingredient.id + '-unit-measurement'"
                v-model="ingredient.unitMeasurement"
                required
              />
            </div>
            <div class="form-controls align-right">
              <button class="btn btn-sm" v-on:click.prevent="removeIngredient(ingredient)">
                Remove
              </button>
            </div>
          </li>
        </ul>
        <div class="form-controls">
          <button class="btn btn-sm"
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
            <button class="btn btn-sm" v-on:click.prevent="closeIngredientSearch">Cancel</button>
          </div>
          
        </div>
        <div class="add-new-ingredient" v-show="isAddNewIngredientOpen">
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
            <label for="newIngredientName">Ingredient Category</label>
            <input
              type="text"
              id="newIngredientCategory"
              v-model="newIngredient.category"
              required
            />
          </div>
          <div class="form-controls">
            <button class="btn btn-sm" v-on:click.prevent="saveNewIngredient">Save</button>
            <button class="btn btn-sm" v-on:click.prevent="closeAddNewIngredient">Cancel</button>
          </div>
        </div>
      </fieldset>
      <fieldset class="recipe-image">
        <legend>Recipe Image</legend>
        <div class="field">
          <label for="image">Image</label>
          <input type="file" id="image" v-on:change="onFileChange" required />
        </div>
        <div class="form-controls">
          <button v-on:click.prevent="chooseImage" class="btn btn-sm">Choose File</button>
        </div>
        <div class="image-preview" v-if="image">
          <img v-bind:src="image" />
        </div>
      </fieldset>
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
    chooseImage() {
      document.getElementById("image").click();
    }
  },
  computed: {
    hasIngredients() {
      return this.ingredients.length > 0;
    },
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
  border-color: #4aae9b;

}
div.image-preview img {
  max-width: 200px;
}
input#image {
  display: none;
}
div.recipe-form {
  padding: 30px;
  width: 50%;
  background-color: white;
}
div.recipe-form fieldset {
  margin: 10px;
  margin-bottom: 20px;
}
ul.ingredients {
  list-style: none;
  padding: 0px;
  margin: 10px;
}
div.recipe-form fieldset {
  /* border-radius: 10px; */
  border: none;
  border-top: solid 1.5px #4aae9b;
  position: relative;
  margin-top: 50px;
  padding-bottom: 30px;
}
div.recipe-form fieldset legend {
  font-weight: bold;
  position: absolute;
  top: -35px;
  left: 0px;

}
li.ingredient {
  border-bottom: solid 1px #ccc;
  padding-top: 10px;
  padding-bottom: 10px;
}
/* div.form-controls {
  display: flex;
  justify-content: flex-end;
} */
</style>
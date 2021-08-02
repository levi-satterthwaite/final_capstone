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
              <input type="text" id="difficultyLevel" v-model="recipe.difficultyLevel" required />
          </div>
          <div>
              <label for="prepTimeMin">Total Prep Time:</label>
              <input type="number" id="prepTimeMin" v-model="recipe.prepTimeMin" required />
          </div>
          <div>
              <label for="cookTimeMin">Total Cook Time:</label>
              <input type="number" id="cookTimeMin" v-model="recipe.cookTimeMin" required />
          </div>
          <div>
              <label for="servingSize">Serving Size:</label>
              <input type="number" id="servingSize" v-model="recipe.servingSize" required />
          </div>
          <div>
              <label for="instructions">Instructions:</label>
              <input type="text" id="instructions" v-model="recipe.instructions" required />
          </div>
          <div>
              <label for="image">Image:</label>
              <input type="file" id="image" v-on:change="onFileChange"/>
          </div>
          <div class="image-preview" v-if="image">
              <img v-bind:src="image">
          </div>
          <button type="submit">Submit</button>
      </form>
  </div>
</template>

<script>
export default {
    name: "recipe-form",
    data() {
        return {
            recipe: {},
            image: '',
            file: ''
        }
    },
    methods: {
        submitRecipe() {
            // $emit() triggers an event called "submit" and passes in recipe 
            // and image as its arguments 
            console.log(this.file);
            const submitData = {
                recipe: this.recipe,
                file: this.file
            }
            console.log("submitData", submitData)
            this.$emit("submit", submitData);
        },
        // whenever we change an input value, this will trigger a change event
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
            // once the reader has finished loading, store the image data into
            // the property image
            reader.onload = (event) => {
                // this event is being targeted by the reader 
                this.image = event.target.result;
            };
            reader.readAsDataURL(file);
        }
    }
}
</script>

<style>
div.image-preview img {
    max-width: 200px;
}
</style>
<template>
 <div class="recipe-details">
 <p class="description">{{ description }}</p>
 <recipe-card v-bind:recipe = "recipe"/> <!-- this is our template for recipe binding to object -->  
 
 <div id="recipeDetail">
     <h1>{{recipe.name}}</h1>
      <img :src="imageFileName" />
      <p><a v-bind:href="imageFileName">{{recipe.name}}</a></p>
      <p v-for="ingredient in recipe.ingredientList" 
        v-bind:key="ingredient.id">
        {{ingredient.name}}
       </p>
      <p>{{ recipe.instructions }}</p>

 </div>
 </div>
</template>

<script>
import RecipeCard from '../components/RecipeCard.vue';
export default {
    name: 'recipe-detail',
    data () {
        return{
            recipe: undefined,
        }
    },
    created () {
        console.log(this.$route.params.id)
        const findRecipeById = this.$store.state.recipes.find ((r) => {
            if(r.recipeId == this.$route.params.id) {
                return true
            } else {
                return false
            }
        });
        this.recipe = findRecipeById
    },
    components: {
        RecipeCard,
        

    }
    
}
    
</script>

<style>
div.description {
    color: #4B3F72;
}
#recipeDetail {
    text-align: left;
    font-size: 15pt;
    color: #4B3F72;
}
</style>
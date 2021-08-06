<template>
 <div class="recipe-details">
 <p class="description"></p>

 <recipe-card v-bind:recipe = "recipe"/> <!-- this is our template for recipe binding to object -->  
 </div>
</template>

<script>
import RecipeCard from '../components/RecipeCard.vue';
export default {
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

</style>
<template>
 <div class="recipe-details">
 <p class="description"></p>

 <recipe-card/>  
 </div>
</template>

<script>
/*import mealPlannerService from '@/services/MealPlannerService';*/
import RecipeCard from '../components/RecipeCard.vue';
export default {
    components: {
        RecipeCard,
    }
    
}
    
    


</script>

<style>

</style>
<template>
 <div class="recipe-details">
 </div>
</template>

<script>
import mealPlannerService from '@/services/mealPlannerService';
export default {
    name: 'recipe-details',

    

      }
    
    methods: {
        getRecipeDetails() {
            mealPlannerService.getRecipeById()
            

        }
    }
    


</script>

<style>

</style>
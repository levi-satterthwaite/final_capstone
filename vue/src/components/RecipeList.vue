<template>
  <div>
      <recipe-card />
  </div>
</template>

<script>
import mealPlannerService from '@/services/MealPlannerService';
import RecipeCard from '@/components/RecipeCard'

export default {
    name: 'recipe-list',
    data() {
        return {
            recipes: []
        }
    },
    components: {
        RecipeCard
    },
    methods: {
        getAllRecipes() {
            mealPlannerService
        }
    }
}
</script>

<style>

</style>
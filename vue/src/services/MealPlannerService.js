import axios from 'axios';

const http = axios.create({
    baseURL: "http://localhost:8080"
});

export default {

    getRecipes(recipeName) {
        let name = '';
        if (typeof recipeName === "string") {
            name = recipeName;
        }
        return http.get(`/recipes?name=${name}`);
    },

    getRecipeById(id) {
        return http.get(`/recipes/${id}`);
    },

    addRecipe(recipe) {
        return http.post('/recipes', recipe);
    },

    addImage(file) {
        const formData = new FormData();
        // attaching a file to our request by the key 'file'
        // see Postman for example
        formData.append('file', file);
        return http.post('/files', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    },

    getIngredients(ingredientName) {
        let name = '';
        if (typeof ingredientName === "string") {
            name = ingredientName;
        }
        return http.get(`/ingredients?name=${name}`);
    },

    addIngredient(ingredient) {
        return http.post('/ingredients', ingredient);
    },

    addIngredientsToRecipe(recipe, ingredients) {
        return http.post(`/recipes/${recipe.recipeId}/ingredients`, ingredients);
    },

    getError(e) {
        if (!e) {
            return "Internal error occurred";
        } else if (e.response && e.response.data) {
            return e.response.data;
        } else if (e.request) {
            return "Server did not respond";
        } 
        return e;
    },

    getMealPlans(mealPlanName) {
        let name = '';
        if (typeof mealPlanName === "string") {
            name = mealPlanName;
        }
        return http.get(`/mealplans?name=${name}`);
    },

    getMealPlanById(id) {
        return http.get(`/mealplans/${id}`);
    },

    addMealPlan(mealPlan) {
        return http.post('/mealplans', mealPlan);
    },

    addRecipesToMealPlan(mealPlan, recipes) {
        return http.post(`/mealplans/${mealPlan.mealId}/recipes`, recipes);
    },

    getRecipeCategories() {
        return http.get('/recipes/categories');
    },

    getRecipeDifficultyLevels() {
        return http.get('/recipes/difficultylevels');
    }

}
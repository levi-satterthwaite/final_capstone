import axios from 'axios';

// axios.create() is like creating a new instance of axios (new axios())
// {baseURL: http://localhost:8080"} is the configuration (instance variable)

// const http = axios.create({
//     baseURL: "http://localhost:8080"
// });

// we will need to create a new instance of axios for each service method so that if 
// axios.defaults.headers.common is changed (by logging in or logging out), the instance will have 
// the correct configurations (authorization header)
const baseURL = 'http://localhost:8080';
const axiosConfig = {baseURL};

export default {

    getRecipes(recipeName) {
        const http = axios.create(axiosConfig);
        let name = '';
        if (typeof recipeName === "string") {
            name = recipeName;
        }
        return http.get(`/recipes?name=${name}`);
    },

    getRecipeById(id) {
        const http = axios.create(axiosConfig);
        return http.get(`/recipes/${id}`);
    },

    addRecipe(recipe) {
        const http = axios.create(axiosConfig);
        return http.post('/recipes', recipe);
    },

    updateRecipe(recipe) {
        const http = axios.create(axiosConfig);
        return http.put(`/recipes/${recipe.recipeId}`, recipe);
    },

    deleteRecipe(recipe) {
        const http = axios.create(axiosConfig);
        return http.delete(`/recipes/${recipe.recipeId}`, recipe);
    },

    addImage(file) {
        const http = axios.create(axiosConfig);
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
        const http = axios.create(axiosConfig);
        let name = '';
        if (typeof ingredientName === "string") {
            name = ingredientName;
        }
        return http.get(`/ingredients?name=${name}`);
    },

    addIngredient(ingredient) {
        const http = axios.create(axiosConfig);
        return http.post('/ingredients', ingredient);
    },

    addIngredientsToRecipe(recipe, ingredients) {
        const http = axios.create(axiosConfig);
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

    getMeals(mealName) {
        const http = axios.create(axiosConfig);
        let name = '';
        if (typeof mealName === "string") {
            name = mealName;
        }
        return http.get(`/meals?name=${name}`);
    },

    getMealById(id) {
        const http = axios.create(axiosConfig);
        return http.get(`/meals/${id}`);
    },

    addMeal(meal) {
        const http = axios.create(axiosConfig);
        return http.post('/meals', meal);
    },

    updateMeal(meal) {
        const http = axios.create(axiosConfig);
        return http.put(`/meals/${meal.mealId}`, meal);
    },

    deleteMeal(meal) {
        const http = axios.create(axiosConfig);
        return http.delete(`/meals/${meal.mealId}`, meal);
    },

    addRecipesToMeal(meal, recipes) {
        const http = axios.create(axiosConfig);
        return http.post(`/meals/${meal.mealId}/recipes`, recipes);
    },

    getMealPlans(mealPlanName) {
        const http = axios.create(axiosConfig);
        let name = '';
        if (typeof mealPlanName === "string") {
            name = mealPlanName;
        }
        return http.get(`/mealplans?name=${name}`);
    },

    getMealPlanById(id) {
        const http = axios.create(axiosConfig);
        return http.get(`/mealplans/${id}`);
    },

    addMealPlan(mealPlan) {
        const http = axios.create(axiosConfig);
        return http.post('/mealplans', mealPlan);
    },

    updateMealPlan(mealPlan) {
        const http = axios.create(axiosConfig);
        return http.put(`/mealplans/${mealPlan.mealPlanId}`, mealPlan);
    },

    deleteMealPlan(mealPlan) {
        const http = axios.create(axiosConfig);
        return http.delete(`/mealplans/${mealPlan.mealPlanId}`, mealPlan);
    },

    addMealsToMealPlan(mealPlan, meals) {
        const http = axios.create(axiosConfig);
        return http.post(`/mealplans/${mealPlan.mealPlanId}/meals`, meals);
    },

    getRecipeCategories() {
        const http = axios.create(axiosConfig);
        return http.get('/recipes/categories');
    },

    getRecipeDifficultyLevels() {
        const http = axios.create(axiosConfig);
        return http.get('/recipes/difficultylevels');
    },

    getMealCategories() {
        const http = axios.create(axiosConfig);
        return http.get('/meals/categories');
    }

}
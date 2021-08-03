import axios from 'axios';

const http = axios.create({
    baseURL: "http://localhost:8080"
});

export default {

    getRecipes() {
        return http.get('/recipes');
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
        if(typeof ingredientName === "string") {
            name = ingredientName;
        }
        return http.get(`/ingredients?name=${name}`);
    },

    addIngredient(ingredient) {
        return http.post('/ingredients', ingredient);
    }

}
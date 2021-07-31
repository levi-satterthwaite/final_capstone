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
    }

}
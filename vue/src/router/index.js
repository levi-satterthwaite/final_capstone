import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
import Recipes from '../views/Recipes.vue'
import AddRecipe from '../views/AddRecipe.vue'
import RecipeDetails from '../views/RecipeDetails.vue'
import Meals from '../views/Meals.vue'
import AddMeal from '../views/AddMeal.vue'
import MealPlans from '../views/MealPlans.vue'
import MealDetails from '../views/MealDetails.vue'
import AddMealPlan from '../views/AddMealPlan.vue'
import MealPlanDetails from '../views/MealPlanDetails.vue'
import UpdateMealPlan from '../views/UpdateMealPlan.vue'
import UpdateMeal from '../views/UpdateMeal.vue'

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/recipes",
      name: "recipes",
      component: Recipes,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/recipes/add",
      name: "addRecipe",
      component: AddRecipe,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/recipes/:id",
      name: "recipeDetails",
      component: RecipeDetails,
      meta: {
        requiresAuth: false
      }
    },  
    {
      path: "/meals",
      name: "meals",
      component: Meals,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/meals/add",
      name: "addMeal",
      component: AddMeal,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/meals/:id/edit",
      name: "updateMeal",
      component: UpdateMeal,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/meals/:id",
      name: "mealDetails",
      component: MealDetails,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/mealplans",
      name: "mealPlans",
      component: MealPlans,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/mealplans/add",
      name: "addMealPlan",
      component: AddMealPlan,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/mealplans/:id/edit",
      name: "updateMealPlan",
      component: UpdateMealPlan,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/mealplans/:id",
      name: "mealPlanDetails",
      component: MealPlanDetails,
      meta: {
        requiresAuth: false
      }
    }
  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;

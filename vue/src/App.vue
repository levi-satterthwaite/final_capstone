<template>
  <div id="app">
    <div id="nav">
      <div class="toolbar">
        <router-link v-bind:to="{ name: 'home' }">Home</router-link
        >&nbsp;|&nbsp;
        <router-link v-bind:to="{ name: 'recipes' }">Recipes</router-link
        >&nbsp;|&nbsp;
         <router-link v-bind:to="{ name: 'mealPlans' }">Meal Plans</router-link
        >&nbsp;|&nbsp;
        <router-link
          v-bind:to="{ name: 'logout' }"
          v-if="$store.state.token != ''"
          >Logout</router-link
        >
        <router-link
          v-bind:to="{ name: 'login' }"
          v-if="$store.state.token == ''"
          >Login</router-link
        >
      </div>
    </div>
    <div class="router-view">
      <router-view />
    </div>
  </div>
</template>

<style>
* {
  font-family: Rubik, "Poppins", sans-serif;
}
#nav {
  background-color: #4B3F72;
  height: 75px;
  z-index:1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  position: fixed;
  top: 0;
  width: 100%;
}
div.toolbar {
  display: flex;
  margin-right: 20px;
  color: white;
}
div.toolbar a {
  margin-right: 10px;
  margin-left: 10px;
  color: white;
}
div.toolbar a:hover {
  color: #cccccc;
}
div.router-view {
  margin-top: 75px;
}
/* div.toolbar a:last-child {
  margin-right: 20px;
} */
body {
  background-color: rgb(245, 245, 245);
}
.card {
  margin: 10px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  border-radius: 10px;
}

.btn {
  background-color: #9D7DDE;
  color: white;
  padding: 15px;
  font-size: 15px;
  border: none;
  margin: 10px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
  border-radius: 10px;
}
.btn-sm {
  font-size: 14px;
  padding: 10px;
}
.align-right {
  display: flex;
  justify-content: flex-end;
}
</style>

<template>
  <div id="app">
    <div id="nav">
      <div class="toolbar">
        <router-link v-bind:to="{ name: 'home' }">Home</router-link
        >&nbsp;|&nbsp;
        <router-link v-bind:to="{ name: 'recipes' }">Recipes</router-link
        >&nbsp;|&nbsp;
        <router-link v-bind:to="{ name: 'meals' }">Meals</router-link
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
a {
  display: block;
  text-decoration: none;
  color: #4b3f72;
}

a:hover {
  color: #9d7dde;
}

#nav {
  background-color: #4b3f72;
  height: 75px;
  z-index: 1;
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
  margin-bottom: 80px;
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
  background-color: #9d7dde;
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

div.page {
  display: flex;
  flex-direction: column;
  margin-left: 50px;
  margin-right: 50px;
  align-items: center;
}

div.page h1 {
  text-align: center;
  margin-top: 50px;
  color: #4b3f72
}

div.page div.container {
  display: flex;
  justify-items: center;
}

div.action-bar {
  display: flex;
  justify-content: flex-end;
  position: fixed;
  left: 0;
  bottom: 0;
  width:  calc(100% - 100px);
  text-align: center;
  margin-left: 50px;
  margin-right: 50px;
  margin-bottom: 25px
}
</style>

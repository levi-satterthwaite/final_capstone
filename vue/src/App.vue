<template>
  <div id="app">
    <header id="nav">
      <img src="@/assets/feed_me.jpg" alt="Feed Me Logo" class="feed-me-logo" />
      <div class="toolbar">
        <div class="nav-links">
          <router-link v-bind:to="{ name: 'home' }">Home</router-link>
          <!-- &nbsp;|&nbsp; -->
          <router-link v-if="$store.state.token" v-bind:to="{ name: 'recipes' }">Recipes</router-link>
          <!-- &nbsp;|&nbsp; -->
          <router-link v-if="$store.state.token" v-bind:to="{ name: 'meals' }">Meals</router-link>
          <!-- &nbsp;|&nbsp; -->
          <router-link v-if="$store.state.token" v-bind:to="{ name: 'mealPlans' }">Meal Plans</router-link>
          <!--&nbsp;|&nbsp;-->
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
    </header>
    <div class="router-view">
      <router-view />
    </div>
  </div>
</template>

<style>
* {
  font-family: Rubik, "Poppins", sans-serif;
}
#app {
  display: flex;
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
  height: 78px;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  position: fixed;
  top: 0;
  width: 100%;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

#nav .nav-links {
  display: inherit;
}

.nav-links a {
  color: white;
}

.nav-links a::after {
  content: "\00a0|\00a0";
  color: white;
}

.nav-links a:hover {
  color: #cccccc;
}

.nav-links a::after:hover {
  color: white;
}
.nav-links a:last-child::after {
  content: unset;
}
.nav-links a:last-child {
  margin-right: 10px;
}

div.toolbar {
  display: flex;
  margin-right: 20px;
  color: white;
  font-size: 18px;
}
div.router-view {
  margin-top: 91px;
  margin-bottom: 80px;
  display: flex;
  flex-grow: 1;
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
  flex-grow: 1;
  padding-top: 25px;
}

div.page h1 {
  text-align: center;
  color: #4b3f72;
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
  width: calc(100% - 100px);
  text-align: center;
  margin-left: 50px;
  margin-right: 50px;
  margin-bottom: 25px;
}
div.form div.field {
  margin: 10px;
  margin-bottom: 20px;
}
div.form div.field label {
  /* display: none; */
  display: block;
  /* width: 175px;
  display: inline-block; */
  margin-bottom: 5px;
}
div.form div.field input,
div.form div.field select,
div.form div.field textarea,
div.page div.field input {
  -ms-box-sizing: content-box;
  -moz-box-sizing: content-box;
  -webkit-box-sizing: content-box;
  box-sizing: content-box;
  width: calc(100% - 20px);
  font-size: 16px;
  line-height: 30px;
  border: none;
  border-color: #4b3f72;
  border-style: solid;
  border-radius: 10px;
  border-width: 1.5px;
  padding: 5px;
  padding-left: 10px;
  padding-right: 10px;
}
div.form div.field input:focus,
div.form div.field select:focus,
div.form div.field textarea:focus {
  outline: none;
  border-color: #9d7dde;
}
div.form div.field select {
  height: 30px;
}
div.form div.field textarea {
  resize: none;
}
div.image-preview img {
  max-width: 200px;
}
input#image {
  display: none;
}
div.form {
  padding: 30px;
  width: 70%;
  background-color: white;
}
div.form fieldset {
  margin: 10px;
  margin-bottom: 20px;
  border: none;
  border-top: solid 1.5px #9d7dde;
  position: relative;
  margin-top: 50px;
  padding-bottom: 30px;
}
div.form fieldset legend {
  color: #4b3f72;
  font-weight: bold;
  position: absolute;
  top: -35px;
  left: 0px;
}
div.form ul.items {
  list-style: none;
  padding: 0px;
  margin: 0px;
}
div.form ul.items li {
  border-bottom: solid 1px #ccc;
  padding-top: 10px;
  padding-bottom: 10px;
}
div.error {
  color: red;
}
header#nav img {
  position: fixed;
  top: 0;
  left: 0;
  height: 78px;
}
</style>

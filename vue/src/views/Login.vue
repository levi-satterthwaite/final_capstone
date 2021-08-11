<template>
  <div class="page">
    <div id="login" class="form text-center card">
      <form class="form-signin" @submit.prevent="login">
        <h2 class="h3 mb-3 font-weight-normal">Please Sign In</h2>
        <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div
          class="alert alert-success"
          role="alert"
          v-if="this.$route.query.registration"
        >
          Thank you for registering, please sign in.
        </div>
        <div class="field">
          <label for="username" class="sr-only">Username</label>
          <input
            type="text"
            id="username"
            class="form-control"
            placeholder="Username"
            v-model="user.username"
            required
            autofocus
          />
        </div>
        <div class="field">
          <label for="password" class="sr-only">Password</label>
          <input
            type="password"
            id="password"
            class="form-control"
            placeholder="Password"
            v-model="user.password"
            required
          />
        </div>
        <div class="register">
           <router-link :to="{ name: 'register' }">Need an account?</router-link>
        </div>
        <div class="form-controls align-right">
          <button class="btn" type="submit">Sign in</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>

<style>
div#login {
  margin-top: 75px;
}
div#login h2 {
  margin-top: 20px;
  margin-bottom: 40px;
  color: #4b3f72;
  font-size: 35px;
}
div#login div.field label {
  font-size: 20px;
  color: #4b3f72;
}
div#login div.field input#username {
  font-size: 18px;
}
div#login div.field input#password {
  font-size: 18px;
}
div.register {
  margin: 10px;
  font-size: 18px;
}
div#login div.form-controls button.btn {
  font-size: 17px;
}
</style>

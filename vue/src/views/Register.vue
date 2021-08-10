<template>
  <div class="page">
    <div id="register" class="form text-center card">
      <form class="form-register" @submit.prevent="register">
        <h2 class="h3 mb-3 font-weight-normal">Create Account</h2>
        <div class="alert alert-danger" role="alert" v-if="registrationErrors">
          {{ registrationErrorMsg }}
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
          <input
            type="password"
            id="confirmPassword"
            class="form-control"
            placeholder="Confirm Password"
            v-model="user.confirmPassword"
            required
          />
        </div>
        <div class="login">
          <router-link :to="{ name: 'login' }">Have an account?</router-link>
        </div>
        <div class="form-controls align-right">
          <button class="btn btn-lg btn-primary btn-block" type="submit">
            Create Account
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "register",
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "user",
      },
      registrationErrors: false,
      registrationErrorMsg: "There were problems registering this user.",
    };
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = "Password & Confirm Password do not match.";
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = "Bad Request: Validation Errors";
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = "There were problems registering this user.";
    },
  },
};
</script>

<style>
div#register {
  margin-top: 50px;
}
div#register h2 {
  margin-top: 20px;
  margin-bottom: 40px;
  color: #4b3f72;
  font-size: 35px;
}
div#register div.field label {
  font-size: 20px;
  color: #4b3f72;
}
div#register div.field input#username {
  font-size: 18px;
}
div#register div.field input#password {
  font-size: 18px;
}
div#register div.field input#confirmPassword {
  margin-top: 10px;
  font-size: 18px;
}
div.login {
  margin: 10px;
  font-size: 18px;
}
div#register div.form-controls button.btn {
  font-size: 17px;
}
</style>

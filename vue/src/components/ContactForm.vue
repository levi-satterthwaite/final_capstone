<template>
  <div class="page">
    <div class="error" v-if="error">{{ error.message }}</div>
    <form class="form contact-form card" @submit.prevent="sendEmail">
      <label>Name</label>
      <input type="text" name="user_name" />
      <label>Email</label>
      <input type="email" name="user_email" />
      <label>Message</label>
      <textarea name="message" v-model="shareMessage">
      </textarea>
      <input type="submit" value="Send" />
    </form>
  </div>
</template>

<script>
import mealPlannerService from "@/services/MealPlannerService";
import emailjs from "emailjs-com";

export default {
  name: "contact-form",
  data() {
    return {
      error: null,
      shareMessage: ""
    };
  },
  props: {
    message: String
  },
  methods: {
    sendEmail(e) {
      emailjs
        .sendForm(
          "default_service",
          "template_73gexhm",
          e.target,
          "user_izFhIscE8Iov5ws1HU6E4"
        )
        .then(
          (result) => {
            console.log("SUCCESS!", result.status, result.text);
            this.$emit("success");
          },
          (error) => {
            console.log("FAILED...", error);
            this.$emit("error", error);
            this.error = mealPlannerService.getError(error);
          }
        );
    },
  },
  mounted() {
    this.shareMessage = this.message;
  }
};
</script>

<style>
</style>
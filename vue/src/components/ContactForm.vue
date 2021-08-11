<template>
  <div class="form contact-form card">
    <div class="error" v-if="error">{{ error.message }}</div>
    <form @submit.prevent="sendEmail">
      <div class="field">
        <h3>Email this Grocery List</h3>
        <label>Recipient's Name</label>
        <input type="text" name="user_name" />
      </div>
      <div class="field">
        <label>Recipient's Email</label>
        <input type="email" name="user_email" />
      </div>
      <div class="email-body" v-html="shareMessage"></div>
      <textarea name="message" v-show="false" v-model="shareMessage">
      </textarea>
      <div class="form-controls align-right">
        <input type="submit" class="btn" value="Send" />
      </div>
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
      shareMessage: "",
    };
  },
  props: {
    message: String,
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
  },
};
</script>

<style>
div.contact-form {
  width: 30%;
  color: #4b3f72;
}
div.contact-form h3 {
  font-size: 20px;
}
div.email-body {
  padding-left: 15px;
}
</style>
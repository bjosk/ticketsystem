<script setup>
import { ref } from 'vue';
import axios from '@/services/axios';
import { useRouter } from 'vue-router';
import {useAuthStore  } from "@/stores/auth.js";

const username = ref('');
const password = ref('');
const error = ref('');
const router = useRouter();
const auth = useAuthStore();

const submit = async () => {
  try {
    const response = await axios.post('/login', {
      username: username.value,
      password: password.value
    });

    auth.login(response.data);
    console.log(auth.user);
    await router.push('/home');
  } catch (err) {
    error.value = 'Login failed. Please try again.';
    console.error(err);
  }
};
</script>

<template>
  <form @submit.prevent="submit" class="p-4 border rounded bg-white shadow-sm w-50">
    <h2 class="mb-3 text-center">Login</h2>

    <div class="mb-3">
      <label for="username" class="form-label">Username</label>
      <input
        v-model="username"
        type="text"
        class="form-control"
        id="username"
        placeholder="Enter username"
        required
      />
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Password</label>
      <input
        v-model="password"
        type="password"
        class="form-control"
        id="password"
        placeholder="Enter password"
        required
      />
    </div>

    <button type="submit" class="btn btn-primary w-100">Login</button>

    <div v-if="error" class="alert alert-danger mt-3">
      {{ error }}
    </div>
    <div class="d-flex justify-content-center align-content-center mt-3">
      <p class="me-2">Dont have an account yet? </p>
      <a class="" href="/register">Register</a>
    </div>
  </form>

</template>

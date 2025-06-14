<script setup>
import { ref } from 'vue';
import axios from '@/services/axios';
import { useRouter } from 'vue-router';
import {useAuthStore  } from "@/stores/auth.js";

const username = ref('');
const password = ref('');
const email = ref('');
const error = ref('');
const router = useRouter();
const auth = useAuthStore();

const submit = async () => {
  try {
    const response = await axios.post('/users', {
      username: username.value,
      password: password.value,
      email: email.value,
    });
    await router.push('/login');
  } catch (err) {
    error.value = "Username or email is already in use";
    console.error(err);
  }
};
</script>

<template>
  <form @submit.prevent="submit" class="p-4 border rounded bg-white shadow-sm w-50">
    <h2 class="mb-3 text-center">Register</h2>

    <div class="mb-3">
      <label for="username" class="form-label">Username:</label>
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
      <label for="email" class="form-label">Email:</label>
      <input
        v-model="email"
        type="text"
        class="form-control"
        id="username"
        placeholder="Enter username"
        required
      />
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Password:</label>
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
  </form>
</template>

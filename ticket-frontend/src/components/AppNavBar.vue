<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from "@/stores/auth.js";

const router = useRouter();
const auth = useAuthStore();

const logout = () => {
  auth.logout();
  router.push('/login');
};
</script>

<template>
  <nav class="navbar navbar-expand navbar-light bg-light sticky-top">
    <a class="navbar-brand" href="#">
      <img src="./icons/FireLoge.png" alt="Logo" width="30" class="d-inline-block align-text-top" />
      TicketSystem
    </a>

    <div class=" container-fluid">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link class="nav-link" to="/home">Home</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/ticket/new">Raise Ticket</router-link>
        </li>
        <li v-if="auth.user?.role !== 'USER'" class="nav-item">
          <router-link class="nav-link" to="/users/modify">Modify user</router-link>
        </li>
      </ul>
      <div class="nav-item me-2 d-flex align-items-center">
        <p class="nav-item me-2 mb-0">{{ auth.user?.username }}</p>
        <button class="btn btn-outline-danger me-2" @click="logout">Logout</button>
      </div>

    </div>
  </nav>
</template>

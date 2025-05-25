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
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">
      <img src="./icons/FireLoge.png" alt="Logo" width="30" height="30" class="d-inline-block align-text-top" />
      TicketSys
    </a>

    <div class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link class="nav-link" to="/home">Home</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" to="/raise-ticket">Raise Ticket</router-link>
        </li>
      </ul>

      <button class="btn btn-outline-danger" @click="logout">Logout</button>
    </div>
  </nav>
</template>

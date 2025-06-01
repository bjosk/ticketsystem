<script setup>

import AppNavBar from "@/components/AppNavBar.vue";
import {useAuthStore} from "@/stores/auth.js";

const auth = useAuthStore();
</script>

<template>
  <AppNavBar/>
  <div class="container bg-body-tertiary mt-4 pt-2 pb-2">
    <div class="container p-2">
      <label for="shortDescription" class="form-label fw-bold">Username</label>
      <input type="text"
             id="shortDescription"
             class="form-control"
             :disabled="auth.user?.role !== 'AGENT'"
             :value="auth.user?.username"
      />
    </div>
    <div class="container p-2">
      <label for="shortDescription" class="form-label fw-bold">Short description</label>
      <input type="text"
             id="shortDescription"
             class="form-control"
      />
    </div>
    <div class="container p-2">
      <label for="description" class="form-label fw-bold">Description</label>
      <textarea type="text"
                id="description"
                class="form-control"
      />
    </div>
  </div>
</template>

<style scoped>

</style>

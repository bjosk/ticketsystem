<script setup lang="ts">
  import AppNavBar from "@/components/AppNavBar.vue";
  import { useAuthStore } from "@/stores/auth.js";

  const auth = useAuthStore();
</script>

<template>
    <AppNavBar />
  <div class="container mt-4">
    <h2>Your Tickets</h2>

    <!-- ✅ TEMP DEBUG -->
    <pre>User: {{ auth.user }}</pre>
    <pre>User ID: {{ auth.user?.id }}</pre>
  </div>
</template>

<style scoped>

</style>

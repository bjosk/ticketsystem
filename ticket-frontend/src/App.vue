<template>
  <div class="text-center">
    <h1 class="display-4 text-primary">Welcome to the Ticket System</h1>
    <p class="lead">Use the navigation to access pages.</p>
    <RouterLink to="/login" class="btn btn-success mt-3">Go to Login</RouterLink>

    <router-view class="mt-4" />
  </div>
</template>

<script setup lang="ts">
</script>

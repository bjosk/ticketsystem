<script setup>

import AppNavBar from "@/components/AppNavBar.vue";
import { useRoute } from "vue-router";
import { ref, onMounted } from 'vue'
import axios from '@/services/axios';

const route = useRoute();
const ticketId = route.params.id;
const ticket = ref(null)
const error = ref(null)


  onMounted( async () => {
    try {
      const response = await axios.get(`/tickets/${ticketId}`);
      ticket.value = response.data;

      console.log(ticket.value)
    } catch (err) {
      console.log(err);
    }
  })
</script>

<template>
  <AppNavBar />
  <div class="container mt-4">
    <p>{{ ticket }}</p>
  </div>

</template>

<style scoped>

</style>

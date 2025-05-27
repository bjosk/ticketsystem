<script setup>
  import AppNavBar from "@/components/AppNavBar.vue";
  import { useAuthStore } from "@/stores/auth.js";
  import axios from '@/services/axios';
  import { ref, onMounted } from 'vue'

  const auth = useAuthStore();
  const tickets = ref([]);
  const error = ref(null);


  onMounted(async () => {
    try {
      const response = await axios.get(`/users/${auth.user.userId}/tickets`)
      tickets.value = response.data
    } catch (err) {
      console.error(err)
      error.value = 'Could not load tickets.'
    }
  })

</script>

<template>
    <AppNavBar />
  <div class="container mt-4">
    <h2>Your Tickets</h2>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

    <ul class="list-group" v-if="tickets.length">
      <li v-for="ticket in tickets" :key="ticket.ticketId" class="list-group-item">
        <div class="d-flex justify-content-between">
          <h5 class="mb-1">{{ ticket.shortDescription }}</h5>
          <span class="badge bg-secondary">{{ ticket.ticketStatus }}</span>
        </div>
        <p class="mb-1" v-if="ticket.description">{{ ticket.description }}</p>
        <small class="text-muted">
          Created: {{ new Date(ticket.createdAt).toLocaleString() }} |
          Submitted by: {{ ticket.submittedByUsername }} |
          Assigned to: {{ ticket.assignedToUsername || 'Unassigned' }}
        </small>
      </li>
    </ul>
    <p v-else>No tickets found.</p>
  </div>
</template>

<style scoped>

</style>

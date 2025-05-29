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

<!--    <ul class="list-group" v-if="tickets.length">-->
<!--      <li v-for="ticket in tickets" :key="ticket.ticketId" class="list-group-item">-->
<!--        <div class="d-flex justify-content-between">-->
<!--          <h5 class="mb-1">{{ ticket.shortDescription }}</h5>-->
<!--          <span class="badge bg-secondary">{{ ticket.ticketStatus }}</span>-->
<!--        </div>-->
<!--        <div class="d-flex justify-content-between text-muted">-->
<!--          <small>Created: {{ new Date(ticket.createdAt).toLocaleString() }}</small>-->
<!--          <small>Submitted by: {{ ticket.submittedByUsername }}</small>-->
<!--          <small>Assigned to: {{ ticket.assignedToUsername || 'Unassigned' }}</small>-->
<!--        </div>-->
<!--      </li>-->
<!--    </ul>-->

    <table class="table table-striped table-hover border" v-if="tickets">
      <thead class="table-dark">
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Short Description</th>
          <th>Created at</th>
          <th>Submitted by</th>
          <th>Status</th>
          <th>Assigned to</th>
        </tr>
      </thead>
      <tbody class="table-group-divider">
        <tr v-for="ticket in tickets" class="position-relative">
          <td><a href="https://www.vg.no" class="stretched-link text-decoration-none text-reset">{{ ticket.ticketId }}</a></td>
          <td>{{ ticket.shortDescription }}</td>
          <td>{{ ticket.createdAt.slice(0, 10) }}</td>
          <td>{{ ticket.submittedByUsername }}</td>
          <td>{{ ticket.ticketStatus }}</td>
          <td>{{ ticket.assignedToUsername }}</td>
        </tr>
      </tbody>
    </table>
    <p v-else>No tickets found.</p>
  </div>
</template>

<style scoped>

</style>

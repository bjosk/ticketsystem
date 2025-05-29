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

      let response = '';

      if (auth.user.role === 'AGENT') {
        response = await axios.get("/tickets");
      } else {
        response = await axios.get(`/users/${auth.user.userId}/tickets`)
      }

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
    <h2 v-if="auth.user.role === 'AGENT'">All Tickets</h2>
    <h2 v-if="auth.user.role === 'USER'">Your Tickets</h2>

    <div v-if="error" class="alert alert-danger">{{ error }}</div>

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

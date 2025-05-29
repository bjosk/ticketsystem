<script setup>

import AppNavBar from "@/components/AppNavBar.vue";
import { useRoute } from "vue-router";
import { ref, onMounted } from 'vue'
import axios from '@/services/axios';
import {computed} from "vue";

const route = useRoute();
const ticketId = route.params.id;
const ticket = ref({})
const error = ref(null)

const formattedDate = computed(() => {
  if (!ticket.value?.createdAt) return ''
  const date = new Date(ticket.value.createdAt)
  return date.toLocaleString('no-NO', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
})


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
      <form>
        <table class="table table-borderless">
          <tbody>
          <tr class="align-middle">
            <th><label for="ticketId">ID</label></th>
            <td>
              <input
                v-model="ticket.ticketId"
                type="number"
                id="ticketId"
                class="form-control"
                disabled
              />
            </td>
            <th><label for="username">Username</label></th>
            <td>
              <input
                v-model="ticket.submittedByUsername"
                type="text"
                id="username"
                class="form-control"
                disabled
              />
            </td>
            <th><label for="createdAt">Created At</label></th>
            <td>
              <input
                :value="formattedDate"
                type="text"
                id="createdAt"
                class="form-control"
                disabled
              />
            </td>
          </tr>
          <tr class="align-middle">
            <th><label for="status">Status</label></th>
            <td>
              <input
                v-model="ticket.ticketStatus"
                type="text"
                id="status"
                class="form-control"
                disabled
              />
            </td>
            <th><label for="assignedTo">Assigned To</label></th>
            <td>
              <input
                v-model="ticket.assignedToUsername"
                type="text"
                id="assignedTo"
                class="form-control"
                disabled
              />
            </td>
          </tr>
          </tbody>
        </table>
        <div>
          <label for="shortDescription" class="form-label fw-bold">Short description</label>
          <input v-model="ticket.shortDescription"
                 type="text"
                 id="shortDescription"
                 class="form-control"
                 disabled
          />
        </div>
        <div>
          <label for="description" class="form-label fw-bold">Description</label>
          <input v-model="ticket.description"
                 type="text"
                 id="description"
                 class="form-control"
                 disabled
          />
        </div>
      </form>
    </div>

</template>

<style scoped>

</style>

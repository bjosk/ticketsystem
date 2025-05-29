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
        <table class="table table-borderless mb-3">
          <tbody>
          <tr class="align-middle">
            <th class="pe-0"><label for="ticketId" class="input-group-text fw-bold">ID</label></th>
            <td class="ps-0">
              <input
                v-model="ticket.ticketId"
                type="number"
                id="ticketId"
                class="form-control"
                disabled
              />
            </td>
            <th class="pe-0"><label for="username" class="input-group-text fw-bold">Username</label></th>
            <td class="ps-0">
              <input
                v-model="ticket.submittedByUsername"
                type="text"
                id="username"
                class="form-control"
                disabled
              />
            </td>
            <th class="pe-0"><label for="createdAt" class="input-group-text fw-bold">Created At</label></th>
            <td class="ps-0">
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
            <th class="pe-0"><label for="status" class="input-group-text fw-bold">Status</label></th>
            <td class="ps-0">
              <input
                v-model="ticket.ticketStatus"
                type="text"
                id="status"
                class="form-control"
                disabled
              />
            </td>
            <th class="pe-0"><label for="assignedTo" class="input-group-text fw-bold">Assigned To</label></th>
            <td class="ps-0">
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
        <div class="container mb-3">
          <label for="shortDescription" class="form-label fw-bold">Short description</label>
          <input v-model="ticket.shortDescription"
                 type="text"
                 id="shortDescription"
                 class="form-control"
                 disabled
          />
        </div>
        <div class="container mb-3">
          <label for="description" class="form-label fw-bold">Description</label>
          <textarea v-model="ticket.description"
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

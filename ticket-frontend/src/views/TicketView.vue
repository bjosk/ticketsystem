<script setup>

import AppNavBar from "@/components/AppNavBar.vue";
import { useRoute } from "vue-router";
import { ref, onMounted } from 'vue'
import axios from '@/services/axios';
import {computed} from "vue";
import NewComment from "@/components/NewComment.vue";
import {useAuthStore} from "@/stores/auth.js";
import SearchAllUsers from "@/components/SearchAllUsers.vue";
import Footer from "@/components/Footer.vue";

const auth = useAuthStore();
const route = useRoute();
const ticketId = route.params.id;
const ticket = ref({})
const comments = ref([])
const error = ref(null)
const availableStatuses = ref([]);
const showModalOpen = ref(false)
const showSuccessAlert = ref(false)

const updateTicket = async () => {
  try {
    const payload = {
      ticketStatus: ticket.value.ticketStatus,
      assignedTo: ticket.value.assignedToUsername,
      shortDescription: ticket.value.shortDescription,
      description: ticket.value.description
    }

    await axios.put(`/tickets/${ticketId}`, payload)

    showSuccessAlert.value = true
    setTimeout(() => {
      showSuccessAlert.value = false
    }, 3000)
    console.log('Ticket updated successfully.')
  } catch (err) {
    console.error('Failed to update ticket:', err)
  }
}


//Opens up the search modal for assigning an agent to a ticket
const openUserSearchModal = () => {
  if (auth.user?.role === 'USER') return
  showModalOpen.value = true
}

//After user is selected the username of the selected user is set to the assignedToUsername field of the ticket
const handleUserSelect = (user) => {
  ticket.value.assignedToUsername = user.username
  console.log(ticket.value)
}

//Fetches the TicketStatus enum
const fetchAvailableTicketStatuses = async () => {
  try {
    const response = await axios.get('/tickets/statuses');
    availableStatuses.value = response.data;
  } catch (err) {
    console.error("Failed to load statuses:", err);
  }
}

const fetchComments = async () => {
  try {
    const response = await axios.get(`/tickets/${ticketId}/comments`);
    comments.value = response.data;
  } catch (err) {
    console.error('Failed to fetch comments:', err);
  }
};

// Formatted date for the ticket and reverses the order so that newest tickets are first.
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

//Formats the date on all comments
const formattedComments = computed(() =>
  comments.value.map(comment => ({
    ...comment,
    createdAtFormatted: new Date(comment.createdAt).toLocaleString('no-NO', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  })).reverse()
);

onMounted( async () => {
  try {
    const ticketResponse = await axios.get(`/tickets/${ticketId}`);
    ticket.value = ticketResponse.data;

    await fetchComments();
    await fetchAvailableTicketStatuses();
    console.log(availableStatuses.value)
  } catch (err) {
    console.log(err);
  }

  console.log(ticket.value)
})
</script>

<template>
  <AppNavBar />
  <div v-if="showSuccessAlert" class="alert alert-success alert-dismissible fade show" role="alert">
    Ticket updated successfully.
    <button type="button" class="btn-close" @click="showSuccessAlert = false" aria-label="Close"></button>
  </div>

  <div class="container mt-4 mb-4 bg-body-tertiary rounded">
    <form class="pt-3 pe-3">
      <table class="table table-borderless mb-3 table-bg">
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
            <select
              v-model="ticket.ticketStatus"
              id="status"
              class="form-select"
              :disabled="auth.user?.role !== 'AGENT'"
            >
              <option v-for="status in availableStatuses" :key="status" :value="status">
                {{ status }}
              </option>
            </select>
          </td>
          <th class="pe-0"><label for="assignedTo" class="input-group-text fw-bold">Assigned To</label></th>
          <td class="ps-0">
            <input
              v-model="ticket.assignedToUsername"
              type="text"
              id="assignedTo"
              class="form-control"
              autocomplete="off"
              readonly
              :disabled="auth.user?.role === 'USER'"
              @click.prevent="openUserSearchModal"
              placeholder="Unassigned"
            />
          </td>
          <th>
            <button
              @click.prevent="updateTicket"
              class="btn btn-success"
              v-if="auth.user?.role !== 'USER'"
            >
              Save changes
            </button>
          </th>
        </tr>
        </tbody>
      </table>
      <div class="container mb-3 p-2">
        <label for="shortDescription" class="form-label fw-bold">Short description</label>
        <input v-model="ticket.shortDescription"
               type="text"
               id="shortDescription"
               class="form-control"
               :disabled="auth.user?.role !== 'AGENT'"
        />
      </div>
      <div class="container mb-3 p-2">
        <label for="description" class="form-label fw-bold">Description</label>
        <textarea v-model="ticket.description"
                  type="text"
                  id="description"
                  class="form-control"
                  :disabled="auth.user?.role !== 'AGENT'"
        />
      </div>
    </form>

    <div class="container p-2">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <h6 class="h6 fw-bold mb-0">Comments:</h6>
        <NewComment :ticket-id="ticketId" @commentAdded="fetchComments"/>
        <!--        <button class="btn btn-success">New Comment</button>-->
      </div>
      <div v-if="comments.length === 0" class="list-group pb-4">
        <p>No comments yet!</p>
      </div>
      <div v-if="comments.length !== 0" class="list-group pb-4">
        <div v-for="comment in formattedComments" class="list-group-item">
          <small>{{ comment.createdAtFormatted}} - {{ comment.authorUsername }} commented:</small>
          <p class="mb-0">{{ comment.text }}</p>
        </div>
      </div>
    </div>


  </div>

  <Footer/>

  <SearchAllUsers
    :fetch-agent-and-admin-only="true"
    :show="showModalOpen"
    @close="showModalOpen = false"
    @user-selected="handleUserSelect"
  />

</template>

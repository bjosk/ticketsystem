<script setup>
import AppNavBar from "@/components/AppNavBar.vue";
import { ref } from 'vue'
import { useAuthStore } from "@/stores/auth.js";
import UserSearchModal from "@/components/UserSearchModal.vue"
import axios from '@/services/axios'

const auth = useAuthStore()
const showModal = ref(false)
const selectedUser = ref({
  username: auth.user?.role !== 'USER' ? '' : auth.user?.username || '',
  shortDescription: '',
  description: ''
})
const shortDescription = ref('')
const description = ref('')

const handleUserSelect = (user) => {
  selectedUser.value = user
}

const submitTicket = async () => {

  console.log(auth.user?.username)

  if (!selectedUser.value) {
    alert('Please select a user.')
    return
  }

  const payload = {
    username: selectedUser.value.username,
    shortDescription: shortDescription.value,
    description: description.value
  }

  try {
    await axios.post('/tickets', payload)
    alert('Ticket created successfully.')
    // Optionally reset form here
  } catch (err) {
    console.error('Failed to create ticket:', err)
    alert('Error creating ticket.')
  }
}
</script>

<template>
  <AppNavBar />
  <div class="container bg-body-tertiary mt-4 pt-2 pb-2">
    <div v-if="auth.user?.role !== 'USER'" class="container p-2">
      <label for="userSearch" class="form-label fw-bold">Select User</label>
      <input
        id="userSearch"
        class="form-control"
        type="text"
        placeholder="Click to select a user"
        :value="selectedUser?.username"
        @click="showModal = true"
      />
    </div>

    <div class="container p-2">
      <label for="shortDescription" class="form-label fw-bold">Short description</label>
      <input v-model="shortDescription" type="text" id="shortDescription" class="form-control" />
    </div>

    <div class="container p-2">
      <label for="description" class="form-label fw-bold">Description</label>
      <textarea v-model="description" id="description" class="form-control"></textarea>
    </div>
    <div class="container p-2 text-end">
      <button class="btn btn-success" @click.prevent="submitTicket">Submit Ticket</button>
    </div>

  </div>

  <!-- User search modal -->
  <UserSearchModal
    :show="showModal"
    @close="showModal = false"
    @user-selected="handleUserSelect"
  />
</template>

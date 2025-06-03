<script setup>
import { ref } from 'vue'
import SearchAllUsers from "@/components/SearchAllUsers.vue";
import AppNavBar from "@/components/AppNavBar.vue";
import axios from "@/services/axios.js";
import {useAuthStore} from "@/stores/auth.js";

const auth = useAuthStore();
const searchModalOpen = ref(false)
const selectedUsername = ref('')
const selectedUserObject = ref(null)
const showSuccessAlert = ref(false)

const handleUserSelected = (user) => {
  // Do something with the selected user
  selectedUserObject.value = user
  console.log('Selected user:', user)
}

const modifyUser = async () => {
  const payload = {
    selectedUsername: selectedUsername.value,
    newUsername: selectedUserObject.value.username,
    email: selectedUserObject.value.email,
    role: selectedUserObject.value.role
  }

  try {
    const response = await axios.put("/users", payload);

    selectedUsername.value = ''
    selectedUserObject.value = null

    showSuccessAlert.value = true
    setTimeout(() => {
      showSuccessAlert.value = false
    }, 3000)
  } catch (err) {
    console.error("Failed to update user:", err);
    alert("Error updating user.");
  }
  console.log(payload)
}
</script>

<template>
  <AppNavBar/>
  <div v-if="showSuccessAlert" class="alert alert-success alert-dismissible fade show" role="alert">
    Ticket updated successfully.
    <button type="button" class="btn-close" @click="showSuccessAlert = false" aria-label="Close"></button>
  </div>
  <div class="container bg-body-tertiary mt-4 pt-2 pb-2 rounded">
    <h2>Modify user</h2>
    <div class="container p-2">
      <label class="form-label fw-bold">Select user</label>
      <input
        type="text"
        class="form-control"
        :value="selectedUsername"
        readonly
        @click="searchModalOpen = true"
        placeholder="Click to select user"
        autocomplete="off"
      />
    </div>
    <div v-if="selectedUserObject" class="container m-0 p-0">
      <div class="container p-2">
        <label for="modifyUserRole" class="form-label fw-bold">User role</label>
        <select v-model="selectedUserObject.role" id="modifyUserRole" class="form-select" aria-label="Default select example">
          <option :selected="selectedUserObject.role === 'USER'" value="USER">USER</option>
          <option :selected="selectedUserObject.role === 'AGENT'" value="AGENT">AGENT</option>
          <option :disabled="auth !== 'ADMIN'" :selected="selectedUserObject.role === 'ADMIN'" value="ADMIN">ADMIN</option>
        </select>
      </div>

      <div class="container p-2">
        <label for="modifyUsername" class="form-label fw-bold">Username</label>
        <input
          v-model="selectedUserObject.username"
          id="modifyUsername"
          type="text"
          class="form-control"
          placeholder="Choose a username"
          autocomplete="off"
        />
      </div>

      <div class="container p-2">
        <label for="modifyUserEmail" class="form-label fw-bold">User email</label>
        <input
          v-model="selectedUserObject.email"
          id="modifyUserEmail"
          type="text"
          class="form-control"
          placeholder="Choose an email"
          autocomplete="off"
        />
      </div>

      <div class="container p-2 text-end">
        <button @click.prevent="modifyUser" class="btn btn-success">
          Save changes
        </button>
      </div>

    </div>
  </div>

  <SearchAllUsers
    :fetchAgentAndAdminOnly="false"
    :show="searchModalOpen"
    v-model="selectedUsername"
    @user-selected="handleUserSelected"
    @close="searchModalOpen = false"
  />
</template>


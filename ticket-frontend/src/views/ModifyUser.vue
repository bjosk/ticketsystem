<script setup>
import { ref } from 'vue'
import SearchAllUsers from "@/components/SearchAllUsers.vue";
import AppNavBar from "@/components/AppNavBar.vue";

const searchModalOpen = ref(false)
const selectedUsername = ref('')
const selectedUserObject = ref(null)

const handleUserSelected = (user) => {
  // Do something with the selected user
  selectedUserObject.value = user
  console.log('Selected user:', user)
}
</script>

<template>
  <AppNavBar/>
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
    <div v-if="selectedUsername" class="container m-0 p-0">
      <div class="container p-2">
        <label for="modifyUserRole" class="form-label fw-bold">User role</label>
        <select id="modifyUserRole" class="form-select" aria-label="Default select example">
          <option :selected="selectedUserObject.role === 'USER'" value="USER">USER</option>
          <option :selected="selectedUserObject.role === 'AGENT'" value="AGENT">AGENT</option>
          <option :selected="selectedUserObject.role === 'ADMIN'" value="ADMIN">ADMIN</option>
        </select>
      </div>

      <div class="container p-2">
        <label for="modifyUsername" class="form-label fw-bold">Username</label>
        <input
          id="modifyUsername"
          type="text"
          class="form-control"
          :value="selectedUsername"
          placeholder="Choose a username"
          autocomplete="off"
        />
      </div>

      <div class="container p-2">
        <label for="modifyUserEmail" class="form-label fw-bold">User email</label>
        <input
          id="modifyUserEmail"
          type="text"
          class="form-control"
          :value="selectedUserObject.email"
          placeholder="Choose an email"
          autocomplete="off"
        />
      </div>

      <div class="container p-2 text-end">
        <button class="btn btn-success">
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


<script setup>
import { ref } from 'vue'
import SearchAllUsers from "@/components/SearchAllUsers.vue";
import AppNavBar from "@/components/AppNavBar.vue";

const searchModalOpen = ref(false)
const selectedUsername = ref('')

const handleUserSelected = (user) => {
  // Do something with the selected user
  console.log('Selected user:', user)
}
</script>

<template>
  <AppNavBar/>
  <div class="container p-2">
    <label class="form-label fw-bold">Select User</label>
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

  <SearchAllUsers
    :fetchAgentAndAdminOnly="false"
    :show="searchModalOpen"
    v-model="selectedUsername"
    @user-selected="handleUserSelected"
    @close="searchModalOpen = false"
  />
</template>


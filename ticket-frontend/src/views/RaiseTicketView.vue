<script setup>
import AppNavBar from "@/components/AppNavBar.vue";
import { ref } from 'vue'
import { useAuthStore } from "@/stores/auth.js";
import UserSearchModal from "@/components/UserSearchModal.vue"

const auth = useAuthStore()
const showModal = ref(false)
const selectedUser = ref(null)

const handleUserSelect = (user) => {
  selectedUser.value = user
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
        :value="selectedUser?.username || 'Click to select user'"
        @click="showModal = true"
      />
    </div>

    <div class="container p-2">
      <label for="shortDescription" class="form-label fw-bold">Short description</label>
      <input type="text" id="shortDescription" class="form-control" />
    </div>

    <div class="container p-2">
      <label for="description" class="form-label fw-bold">Description</label>
      <textarea id="description" class="form-control"></textarea>
    </div>
  </div>

  <!-- User search modal -->
  <UserSearchModal
    :show="showModal"
    @close="showModal = false"
    @user-selected="handleUserSelect"
  />
</template>

<!--
  SearchAllUsers.vue

  A reusable modal component for searching and selecting a user.

  Props:
    - show (Boolean): Controls whether the modal is visible.
    - users (Array): Optional static list of users.
    - label (String): Title shown in the modal header.
    - placeholder (String): Placeholder text for the search input.
    - fetchUsers (Boolean): Whether to fetch users from the backend API on mount.
    - fetchAgentAndAdminOnly (Boolean): Whether to fetch only users of type AGENT or ADMIN

  Emits:
    - update:modelValue: Emits the selected user's `username` (supports v-model).
    - user-selected: Emits the full selected user object.
    - close: Instructs the parent to hide the modal (parent must handle visibility).
-->

<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from '@/services/axios'

// Props passed from parent
const props = defineProps({
  show: Boolean,
  users: {
    type: Array,
    default: () => []
  },
  label: {
    type: String,
    default: 'Select User'
  },
  placeholder: {
    type: String,
    default: 'Click to select user'
  },
  fetchUsers: {
    type: Boolean,
    default: true
  },
  fetchAgentAndAdminOnly: {
    type: Boolean,
    default: false
  }
})


// Events emitted to parent
const emit = defineEmits(['update:modelValue', 'user-selected', 'close'])

// Reactive state
const search = ref('')                // Search input model
const internalUsers = ref([])         // Full list of users
const filteredUsers = ref([])         // Filtered search results

// On component mount, fetch users or use props
onMounted(async () => {
  if (props.fetchUsers) {
    try {
      const endpoint = props.fetchAgentAndAdminOnly
        ? '/users/searchAgentAndAdmin'
        : '/users/searchAllUsers'

      const res = await axios.get(endpoint, { params: { usernameQuery: '' } })
      internalUsers.value = res.data
    } catch (err) {
      console.error('Failed to load users:', err)
      internalUsers.value = []
    }
  } else {
    internalUsers.value = props.users
  }

  filteredUsers.value = internalUsers.value
})


// Watch the search query and update filtered list
watch(search, (query) => {
  const q = query.toLowerCase()
  filteredUsers.value = internalUsers.value.filter(user =>
    user.username.toLowerCase().includes(q)
  )
})

// When modal opens/closes, reset or repopulate state
watch(() => props.show, (isOpen) => {
  if (!isOpen) {
    search.value = ''
    filteredUsers.value = []
  } else {
    filteredUsers.value = internalUsers.value
  }
})

// Select a user from the list and emit relevant events
const selectUser = (user) => {
  emit('update:modelValue', user.username) // Supports v-model
  emit('user-selected', user)              // Pass full user object to parent
  emit('close')                            // Ask parent to close modal
  console.log(user)
}
</script>

<template>
  <!-- Modal overlay, only shown if 'show' prop is true -->
  <div v-if="show" class="modal d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <!-- Modal header with label and close button -->
        <div class="modal-header">
          <h5 class="modal-title">{{ label }}</h5>
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>

        <!-- Modal body with search input and user list -->
        <div class="modal-body">
          <!-- Search field -->
          <input
            v-model="search"
            class="form-control mb-3"
            :placeholder="placeholder"
            autocomplete="off"
          />

          <!-- User result list -->
          <ul class="list-group">
            <li
              v-for="user in filteredUsers"
              :key="user.userId"
              class="list-group-item list-group-item-action"
              @click="selectUser(user)"
            >
              <strong>{{ user.username }}</strong><br />
              <small>{{ user.email }} — {{ user.role }}</small>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

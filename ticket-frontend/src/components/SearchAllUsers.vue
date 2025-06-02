<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from '@/services/axios'

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
  }
})

const emit = defineEmits(['update:modelValue', 'user-selected', 'close'])

const search = ref('')
const internalUsers = ref([])
const filteredUsers = ref([])

onMounted(async () => {
  if (props.fetchUsers) {
    try {
      const res = await axios.get('/users/searchAllUsers', {
        params: { usernameQuery: '' }
      })
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

watch(search, (query) => {
  const q = query.toLowerCase()
  filteredUsers.value = internalUsers.value.filter(user =>
    user.username.toLowerCase().includes(q)
  )
})

watch(() => props.show, (isOpen) => {
  if (!isOpen) {
    search.value = ''
    filteredUsers.value = []
  } else {
    filteredUsers.value = internalUsers.value
  }
})

const selectUser = (user) => {
  emit('update:modelValue', user.username)
  emit('user-selected', user)
  emit('close')
}
</script>

<template>
  <div v-if="show" class="modal d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ label }}</h5>
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <input
            v-model="search"
            class="form-control mb-3"
            :placeholder="placeholder"
            autocomplete="off"
          />
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

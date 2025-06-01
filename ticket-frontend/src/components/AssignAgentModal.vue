<script setup>
import { ref, watch } from 'vue'
import axios from '@/services/axios'

//Prop received for TicketInfo
const props = defineProps({
  show: Boolean
})

//Defines the event that are emitted to TicketInfo
const emit = defineEmits(['close', 'user-selected'])
//Hold the search query and the users found in the query
const search = ref('')
const users = ref([])

//Passes the selected user to the TicketInfo through the user-selected emit
const selectUser = (user) => {
  emit('user-selected', user)
  emit('close')
  search.value = ''
}

//Initial search for agents when the modal is opened
watch(() => props.show, async (isOpen) => {
  if (isOpen) {
    try {
      const res = await axios.get('/users/searchAgentAndAdmin', {
        params: { usernameQuery: '' } // empty query = fetch all
      })
      users.value = res.data
    } catch (err) {
      console.error('Initial fetch failed:', err)
    }
  }
})

//Watches if the search query changes and updates the found users
watch(search, async (usernameQuery) => {
  try {
    const res = await axios.get('/users/searchAgentAndAdmin', {
      params: { usernameQuery }
    })
    users.value = res.data
  } catch (err) {
    console.error('Search failed:', err)
  }
})

</script>

<template>
  <div class="modal d-block" tabindex="-1" v-if="show" style="background: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Search and assign user</h5>
<!--          Emits a close event to TicketInfo when clicked-->
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <input v-model="search" class="form-control mb-3" placeholder="Type username..." />
          <ul class="list-group">
            <li
              v-for="user in users"
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

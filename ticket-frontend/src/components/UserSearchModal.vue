<script setup>
import { ref, watch } from 'vue'
import axios from '@/services/axios'
import debounce from 'lodash/debounce'


const props = defineProps({
  show: Boolean
})

const emit = defineEmits(['close', 'user-selected'])

const search = ref('')
const users = ref([])

watch(() => props.show, async (isOpen) => {
  if (isOpen) {
    try {
      const res = await axios.get('/users/searchAllUsers', {
        params: { usernameQuery: '' }
      })
      users.value = res.data
      console.log("called api")
    } catch (err) {
      console.error('Initial fetch failed:', err)
    }
  }
})

watch(search, debounce(async (usernameQuery) => {
  try {
    const res = await axios.get('/users/searchAllUsers', {
      params: { usernameQuery }
    })
    users.value = res.data
    console.log("called api")
  } catch (err) {
    console.error('Search failed:', err)
  }
}, 1000))

const selectUser = (user) => {
  emit('user-selected', user)
  search.value = ''
  emit('close')
}
</script>

<template>
  <div class="modal d-block" tabindex="-1" v-if="show" style="background: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Search and select user</h5>
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

import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null
  }),
  actions: {
    login(user) {
      this.user = user
      localStorage.setItem('user', JSON.stringify(user))
      console.log(user);
    },
    logout() {
      this.user = null
      localStorage.removeItem('user')
    },
    isAuthenticated() {
      return !!this.user
    },
    getUserRole() {
      return this.user?.role
    }
  }
})

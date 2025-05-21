import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'  // or wherever your component lives

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import TestView from "@/views/TestView.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/test',
    component: TestView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

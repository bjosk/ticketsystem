import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from "@/views/HomeView.vue";
import { useAuthStore } from "@/stores/auth.js";
import TicketView from "@/views/TicketView.vue";
import RaiseTicketView from "@/views/RaiseTicketView.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
    meta: { requiresAuth: true }
  },
  {
    path: '/ticket/:id',
    name: 'ticket',
    component: TicketView,
    meta: { requiresAuth: true }
  },
  {
    path: '/ticket/new',
    name: 'newTicket',
    component: RaiseTicketView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


// ✅ Global navigation guard
router.beforeEach((to, from, next) => {
  const auth = useAuthStore();

  if (to.meta?.requiresAuth && !auth.isAuthenticated()) {
    next('/login');
  } else {
    next();
  }
});

export default router

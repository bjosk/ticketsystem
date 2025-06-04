import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from "@/views/HomeView.vue";
import { useAuthStore } from "@/stores/auth.js";
import TicketView from "@/views/TicketView.vue";
import RaiseTicketView from "@/views/RaiseTicketView.vue";
import ModifyUser from "@/views/ModifyUser.vue";

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
  },
  {
    path: '/users/modify',
    name: 'modifyUsers',
    component: ModifyUser,
    meta: { requiresAuth: true, requiresRoles: ['AGENT', 'ADMIN'] }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


// ✅ Global navigation guard
router.beforeEach((to, from, next) => {
  const auth = useAuthStore();
  console.log(auth.getUserRole())
  // Check authentication
  if (to.meta?.requiresAuth && !auth.isAuthenticated()) {
    return next('/login');
  }

  // Check role-based access
  if (to.meta?.requiresRoles) {
    const userRole = auth.getUserRole(); // Ensure this method or value exists in your store
    if (!to.meta.requiresRoles.includes(userRole)) {
      return next('/home'); // Redirect unauthorized users
    }
  }

  next();
});


export default router

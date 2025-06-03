<script setup>
import AppNavBar from "@/components/AppNavBar.vue";
import SearchAllUsers from "@/components/SearchAllUsers.vue";
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth.js";
import axios from "@/services/axios";
import { useRouter } from "vue-router";

const router = useRouter();
const auth = useAuthStore();

const showModalOpen = ref(false);
const showSuccessAlert = ref(false);

// Pre-fill selected user if current user is a 'USER'
const selectedUser = ref(
  auth.user?.role !== "USER"
    ? null
    : {
      username: auth.user?.username || "",
    }
);

const shortDescription = ref("");
const description = ref("");

// Triggered when a user is selected in SearchAllUsers
const handleUserSelect = (user) => {
  selectedUser.value = user;
};

// Submit ticket
const submitTicket = async () => {
  if (!selectedUser.value || !selectedUser.value.username) {
    alert("Please select a user.");
    return;
  }

  const payload = {
    username: selectedUser.value.username,
    shortDescription: shortDescription.value,
    description: description.value,
  };

  try {
    const response = await axios.post("/tickets", payload);
    const newTicket = response.data;

    showSuccessAlert.value = true;
    setTimeout(() => {
      showSuccessAlert.value = false;
      router.push(`/ticket/${newTicket.ticketId}`);
    }, 500);
    console.log(newTicket)
  } catch (err) {
    console.error("Failed to create ticket:", err);
    alert("Error creating ticket.");
  }
};
</script>

<template>
  <AppNavBar />

  <div
    v-if="showSuccessAlert"
    class="alert alert-success alert-dismissible fade show"
    role="alert"
  >
    Ticket created successfully.
    <button
      type="button"
      class="btn-close"
      @click="showSuccessAlert = false"
      aria-label="Close"
    ></button>
  </div>

  <div class="container bg-body-tertiary mt-4 pt-2 pb-2">
    <h2>Raise a ticket</h2>
    <!-- USER SELECTION FIELD (only if role !== 'USER') -->
    <div v-if="auth.user?.role !== 'USER'" class="container p-2">
      <label for="userSearch" class="form-label fw-bold">Select User</label>
      <input
        id="userSearch"
        class="form-control"
        type="text"
        placeholder="Click to select a user"
        autocomplete="off"
        readonly
        :value="selectedUser?.username"
        @click="showModalOpen = true"
      />
    </div>

    <!-- SHORT DESCRIPTION -->
    <div class="container p-2">
      <label for="shortDescription" class="form-label fw-bold"
      >Short description</label
      >
      <input
        v-model="shortDescription"
        type="text"
        id="shortDescription"
        class="form-control"
      />
    </div>

    <!-- FULL DESCRIPTION -->
    <div class="container p-2">
      <label for="description" class="form-label fw-bold">Description</label>
      <textarea v-model="description" id="description" class="form-control"></textarea>
    </div>

    <!-- SUBMIT BUTTON -->
    <div class="container p-2 text-end">
      <button class="btn btn-success" @click.prevent="submitTicket">
        Submit Ticket
      </button>
    </div>
  </div>

  <!-- SEARCH USER MODAL -->
  <SearchAllUsers
    :show="showModalOpen"
    @close="showModalOpen = false"
    @user-selected="handleUserSelect"
  />
</template>


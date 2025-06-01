<script setup>
import {ref} from "vue";
import axios from '@/services/axios';

import { defineProps } from 'vue';
import {useAuthStore} from "@/stores/auth.js";

const emit = defineEmits(['commentAdded']);

const props = defineProps({
  ticketId: {
    type: [String, Number],
    required: true
  }
});

const submitComment = async () => {
  try {
    await axios.post(`/comments`, {
      authorId: auth.user.userId,
      ticketId: props.ticketId,
      text: newCommentText.value,
    });

    emit("commentAdded"); //Notify TicketInfo that a comment was added

    newCommentText.value = ''; //Resets the newCommentText after comment is submitted

  } catch (err) {
    console.error('Failed to submit comment:', err);
  }
};

const auth = useAuthStore();
const newCommentText = ref('');
</script>

<template>
  <button
          class="btn btn-success"
          data-bs-toggle="modal"
          data-bs-target="#newCommentModal">New Comment</button>

  <div class="modal" id="newCommentModal" tabindex="-1" aria-labelledby="newCommentModal">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="newCommentmodal">Add a comment</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <textarea
            v-model="newCommentText"
            class="form-control"
            rows="4"
            placeholder="Type your comment here..."
          ></textarea>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button v-if="newCommentText" type="button" @click="submitComment" class="btn btn-primary" data-bs-dismiss="modal">Submit</button>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>

</style>

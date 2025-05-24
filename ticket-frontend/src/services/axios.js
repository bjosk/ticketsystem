import axios from 'axios';

// Create a pre-configured Axios instance
const instance = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default instance;

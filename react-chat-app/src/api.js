import axios from "axios";

const API_URL =
  process.env.NODE_ENV === "production"
    ? "https://your-backend.onrender.com" // Render or Railway backend URL
    : "http://localhost:8080";

const api = axios.create({
  baseURL: API_URL,
  withCredentials: true, // For session cookies
});

export default api;

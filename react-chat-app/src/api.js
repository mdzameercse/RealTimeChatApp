import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // Your Spring Boot backend URL
  withCredentials: true, // For session cookies
});

export default api;

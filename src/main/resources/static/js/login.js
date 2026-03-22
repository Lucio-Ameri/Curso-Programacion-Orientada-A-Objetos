import { apiPost, saveStoredUser, setFeedback } from "./api.js";

const form = document.getElementById("loginForm");

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  setFeedback("loginMessage", "");

  const payload = {
    email: document.getElementById("email").value.trim(),
    password: document.getElementById("password").value
  };

  try {
    const response = await apiPost("/api/auth/login", payload);
    saveStoredUser(response.usuario);
    setFeedback("loginMessage", response.mensaje || "Login correcto");
    setTimeout(() => {
      window.location.href = "/dashboard.html";
    }, 500);
  } catch (error) {
    setFeedback("loginMessage", error.message, true);
  }
});
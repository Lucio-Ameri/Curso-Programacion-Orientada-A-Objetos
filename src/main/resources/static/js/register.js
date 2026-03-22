import { apiPost, saveStoredUser, setFeedback } from "./api.js";

const form = document.getElementById("registerForm");

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  setFeedback("registerMessage", "");

  const payload = {
    nombre: document.getElementById("nombre").value.trim(),
    apellido: document.getElementById("apellido").value.trim(),
    email: document.getElementById("email").value.trim(),
    password: document.getElementById("password").value,
    dni: document.getElementById("dni").value.trim()
  };

  try {
    const response = await apiPost("/api/auth/register", payload);
    saveStoredUser(response.usuario);
    setFeedback("registerMessage", response.mensaje || "Usuario registrado correctamente");
    setTimeout(() => {
      window.location.href = "/dashboard.html";
    }, 700);
  } catch (error) {
    setFeedback("registerMessage", error.message, true);
  }
});
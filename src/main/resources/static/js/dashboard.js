import { requireUser, wireLogout } from "./api.js";

const usuario = requireUser();
wireLogout();

const userInfo = document.getElementById("userInfo");

userInfo.innerHTML = `
  <div class="info-item"><strong>ID:</strong> ${usuario.usuarioId}</div>
  <div class="info-item"><strong>Nombre:</strong> ${usuario.nombre}</div>
  <div class="info-item"><strong>Apellido:</strong> ${usuario.apellido}</div>
  <div class="info-item"><strong>Email:</strong> ${usuario.email}</div>
  <div class="info-item"><strong>DNI:</strong> ${usuario.dni}</div>
`;
const DEFAULT_HEADERS = {
  "Content-Type": "application/json"
};

async function parseResponse(response) {
  const contentType = response.headers.get("content-type") || "";
  const isJson = contentType.includes("application/json");
  const data = isJson ? await response.json() : await response.text();

  if (!response.ok) {
    let message = "Ocurrió un error inesperado";

    if (isJson && data) {
      if (data.mensaje) {
        message = data.mensaje;
      } else if (Array.isArray(data.detalles) && data.detalles.length > 0) {
        message = data.detalles.join(" | ");
      } else if (data.error) {
        message = data.error;
      }
    } else if (typeof data === "string" && data.trim()) {
      message = data;
    }

    throw new Error(message);
  }

  return data;
}

export async function apiGet(url) {
  const response = await fetch(url, {
    method: "GET",
    headers: DEFAULT_HEADERS
  });
  return parseResponse(response);
}

export async function apiPost(url, body) {
  const response = await fetch(url, {
    method: "POST",
    headers: DEFAULT_HEADERS,
    body: JSON.stringify(body)
  });
  return parseResponse(response);
}

export async function apiDelete(url) {
  const response = await fetch(url, {
    method: "DELETE",
    headers: DEFAULT_HEADERS
  });

  if (response.status === 204) {
    return null;
  }

  return parseResponse(response);
}

export function getStoredUser() {
  const raw = localStorage.getItem("usuario");
  return raw ? JSON.parse(raw) : null;
}

export function saveStoredUser(usuario) {
  localStorage.setItem("usuario", JSON.stringify(usuario));
}

export function getSelectedAccount() {
  const raw = localStorage.getItem("cuentaSeleccionada");
  return raw ? JSON.parse(raw) : null;
}

export function saveSelectedAccount(cuenta) {
  localStorage.setItem("cuentaSeleccionada", JSON.stringify(cuenta));
}

export function clearSession() {
  localStorage.removeItem("usuario");
  localStorage.removeItem("cuentaSeleccionada");
}

export function requireUser() {
  const usuario = getStoredUser();

  if (!usuario) {
    window.location.href = "/index.html";
    throw new Error("Usuario no autenticado");
  }

  return usuario;
}

export function wireLogout(buttonId = "logoutBtn") {
  const button = document.getElementById(buttonId);
  if (!button) return;

  button.addEventListener("click", () => {
    clearSession();
    window.location.href = "/index.html";
  });
}

export function setFeedback(elementId, message, isError = false) {
  const el = document.getElementById(elementId);
  if (!el) return;

  el.textContent = message || "";
  el.classList.remove("ok", "error");

  if (message) {
    el.classList.add(isError ? "error" : "ok");
  }
}

export function formatDate(dateString) {
  if (!dateString) return "-";
  const date = new Date(dateString);
  return date.toLocaleString("es-AR");
}

export function formatMoney(amount, moneda) {
  const currencyMap = {
    ARS: "ARS",
    USD: "USD",
    EUR: "EUR"
  };

  try {
    return new Intl.NumberFormat("es-AR", {
      style: "currency",
      currency: currencyMap[moneda] || "ARS"
    }).format(amount);
  } catch {
    return `${amount} ${moneda || ""}`.trim();
  }
}

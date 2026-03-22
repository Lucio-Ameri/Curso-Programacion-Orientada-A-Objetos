import {
  apiDelete,
  apiGet,
  apiPost,
  formatMoney,
  requireUser,
  saveSelectedAccount,
  setFeedback,
  wireLogout
} from "./api.js";

const usuario = requireUser();
wireLogout();

const form = document.getElementById("createAccountForm");
const list = document.getElementById("accountsList");
const empty = document.getElementById("accountsEmpty");
const refreshBtn = document.getElementById("refreshAccountsBtn");

const modal = document.getElementById("operationModal");
const closeModalBtn = document.getElementById("closeModalBtn");
const cancelModalBtn = document.getElementById("cancelModalBtn");
const operationForm = document.getElementById("operationForm");
const modalCuentaId = document.getElementById("modalCuentaId");
const modalTipoOperacion = document.getElementById("modalTipoOperacion");
const modalMonto = document.getElementById("modalMonto");
const selectedCurrencyText = document.getElementById("selectedCurrencyText");

let cuentasCache = [];

function findCuentaById(cuentaId) {
  return cuentasCache.find(c => String(c.cuentaId) === String(cuentaId));
}

function renderCurrency() {
  const cuenta = findCuentaById(modalCuentaId.value);
  selectedCurrencyText.textContent = cuenta ? cuenta.moneda : "-";
}

function fillAccountOptions(preselectedId = null) {
  modalCuentaId.innerHTML = "";

  cuentasCache.forEach((cuenta) => {
    const option = document.createElement("option");
    option.value = cuenta.cuentaId;
    option.textContent = `Cuenta #${cuenta.cuentaId} - ${cuenta.tipoCuenta} - ${formatMoney(cuenta.saldo, cuenta.moneda)}`;
    modalCuentaId.appendChild(option);
  });

  if (preselectedId) {
    modalCuentaId.value = String(preselectedId);
  }

  renderCurrency();
}

function openOperationModal(preselectedCuentaId = null) {
  if (!cuentasCache.length) {
    setFeedback("accountMessage", "Primero tenés que crear una cuenta", true);
    return;
  }

  fillAccountOptions(preselectedCuentaId);
  modalTipoOperacion.value = "deposito";
  modalMonto.value = "";
  setFeedback("operationMessage", "");
  modal.classList.remove("hidden");
}

function closeOperationModal() {
  modal.classList.add("hidden");
  operationForm.reset();
  setFeedback("operationMessage", "");
  selectedCurrencyText.textContent = "-";
}

async function loadAccounts() {
  setFeedback("accountMessage", "");
  list.innerHTML = "";

  try {
    const cuentas = await apiGet(`/api/cuentas/usuario/${usuario.usuarioId}`);
    cuentasCache = cuentas || [];

    if (!cuentasCache.length) {
      empty.classList.remove("hidden");
      return;
    }

    empty.classList.add("hidden");

    cuentasCache.forEach((cuenta) => {
      const item = document.createElement("article");
      item.className = "list-item";

      item.innerHTML = `
        <h3>Cuenta #${cuenta.cuentaId} <span class="badge">${cuenta.tipoCuenta}</span></h3>
        <div class="info-grid">
          <div class="info-item"><strong>Usuario:</strong> ${cuenta.usuarioId}</div>
          <div class="info-item"><strong>Saldo:</strong> ${formatMoney(cuenta.saldo, cuenta.moneda)}</div>
          <div class="info-item"><strong>Moneda:</strong> ${cuenta.moneda}</div>
        </div>
        <div class="row-actions">
          <button class="btn primary" data-action="moves">Ver movimientos</button>
          <button class="btn secondary" data-action="operate">Operar</button>
          <button class="btn danger" data-action="delete">Eliminar</button>
        </div>
      `;

      item.querySelector('[data-action="moves"]').addEventListener("click", () => {
        saveSelectedAccount(cuenta);
        window.location.href = `/moves.html?cuentaId=${cuenta.cuentaId}`;
      });

      item.querySelector('[data-action="operate"]').addEventListener("click", () => {
        openOperationModal(cuenta.cuentaId);
      });

      item.querySelector('[data-action="delete"]').addEventListener("click", async () => {
        const ok = confirm(`¿Seguro que querés eliminar la cuenta ${cuenta.cuentaId}?`);
        if (!ok) return;

        try {
          await apiDelete(`/api/cuentas/${cuenta.cuentaId}`);
          setFeedback("accountMessage", "Cuenta eliminada correctamente");
          await loadAccounts();
        } catch (error) {
          setFeedback("accountMessage", error.message, true);
        }
      });

      list.appendChild(item);
    });
  } catch (error) {
    cuentasCache = [];
    empty.classList.add("hidden");
    setFeedback("accountMessage", error.message, true);
  }
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  setFeedback("accountMessage", "");

  const payload = {
    usuarioId: usuario.usuarioId,
    tipoCuenta: document.getElementById("tipoCuenta").value
  };

  try {
    await apiPost("/api/cuentas", payload);
    setFeedback("accountMessage", "Cuenta creada correctamente");
    form.reset();
    await loadAccounts();
  } catch (error) {
    setFeedback("accountMessage", error.message, true);
  }
});

operationForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setFeedback("operationMessage", "");

  const cuentaSeleccionada = findCuentaById(modalCuentaId.value);

  if (!cuentaSeleccionada) {
    setFeedback("operationMessage", "No se encontró la cuenta seleccionada", true);
    return;
  }

  const tipoOperacion = modalTipoOperacion.value;
  const monto = Number(modalMonto.value);

  if (!monto || monto <= 0) {
    setFeedback("operationMessage", "Ingresá un monto válido mayor a 0", true);
    return;
  }

  const payload = {
    monto,
    moneda: cuentaSeleccionada.moneda
  };

  const endpoint =
    tipoOperacion === "deposito"
      ? `/api/cuentas/${cuentaSeleccionada.cuentaId}/depositos`
      : `/api/cuentas/${cuentaSeleccionada.cuentaId}/retiros`;

  try {
    await apiPost(endpoint, payload);
    setFeedback(
      "operationMessage",
      `Movimiento de ${tipoOperacion} realizado correctamente`
    );
    await loadAccounts();

    setTimeout(() => {
      closeOperationModal();
    }, 700);
  } catch (error) {
    setFeedback("operationMessage", error.message, true);
  }
});

modalCuentaId.addEventListener("change", renderCurrency);

refreshBtn.addEventListener("click", loadAccounts);
closeModalBtn.addEventListener("click", closeOperationModal);
cancelModalBtn.addEventListener("click", closeOperationModal);

modal.addEventListener("click", (event) => {
  if (event.target === modal) {
    closeOperationModal();
  }
});

window.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && !modal.classList.contains("hidden")) {
    closeOperationModal();
  }
});

loadAccounts();
